package moscow.createdin.backend.service.auth

import moscow.createdin.backend.config.properties.AkiSecurityProperties
import moscow.createdin.backend.exception.JwtValidationException
import moscow.createdin.backend.mapper.AkiUserMapper
import moscow.createdin.backend.model.cookie.JwtCookies
import moscow.createdin.backend.model.dto.SignInRequestDTO
import moscow.createdin.backend.model.dto.UserRoleDTO
import moscow.createdin.backend.service.RefreshTokenService
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val serverProperties: ServerProperties,
    private val securityProperties: AkiSecurityProperties,
    private val akiUserMapper: AkiUserMapper,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenService: JwtTokenService,
    private val userDetailsService: AkiUserDetailsService,
    private val refreshTokenService: RefreshTokenService
) {

    fun authUser(signIn: SignInRequestDTO): Pair<UserRoleDTO, JwtCookies> {
        val userDetails: UserDetails = doAuth(signIn.email.lowercase(), signIn.password)
        return akiUserMapper.detailsDomainToRoleDto(userDetails) to createAuthenticationTokens(userDetails)
    }

    fun updateTokens(authToken: String, refreshToken: String): Pair<UserRoleDTO, JwtCookies> {
        if (!jwtTokenService.checkTokenValidOrExpired(authToken)) {
            throw JwtValidationException("Invalid auth token")
        }

        val username: String = jwtTokenService.retrieveSubject(refreshToken)
        if (!refreshTokenService.isRefreshTokenValid(username, refreshToken)) {
            throw JwtValidationException("Invalid refresh token for user with username = $username")
        }

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
        return akiUserMapper.detailsDomainToRoleDto(userDetails) to createAuthenticationTokens(userDetails)
    }

    private fun doAuth(email: String, password: String): UserDetails {
        return UsernamePasswordAuthenticationToken(email, password)
            .let { authenticationManager.authenticate(it) }
            .principal as UserDetails
    }

    private fun createAuthenticationTokens(userDetails: UserDetails): JwtCookies {
        val refreshToken: String = jwtTokenService.generateRefreshToken(userDetails)
        refreshTokenService.updateRefreshToken(userDetails.username, refreshToken)

        return JwtCookies(
            authToken = jwtTokenService.generateAuthToken(userDetails),
            refreshToken = refreshToken,
            cookiesValidity = securityProperties.refreshTokenValidity,
            isSecure = serverProperties.ssl?.isEnabled ?: false
        )
    }
}
