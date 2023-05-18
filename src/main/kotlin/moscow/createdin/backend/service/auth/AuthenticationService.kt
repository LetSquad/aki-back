package moscow.createdin.backend.service.auth

import moscow.createdin.backend.config.properties.AkiSecurityProperties
import moscow.createdin.backend.mapper.UserMapper
import moscow.createdin.backend.model.cookie.JwtCookies
import moscow.createdin.backend.model.dto.SignInRequestDTO
import moscow.createdin.backend.model.dto.UserRoleDTO
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val serverProperties: ServerProperties,
    private val securityProperties: AkiSecurityProperties,
    private val userMapper: UserMapper,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenService: JwtTokenService
) {

    fun authUser(signIn: SignInRequestDTO): Pair<UserRoleDTO, JwtCookies> {
        val userDetails: UserDetails = doAuth(signIn.email, signIn.password)
        return userMapper.detailsDomainToRoleDTO(userDetails) to createAuthenticationTokens(userDetails)
    }

    private fun doAuth(email: String, password: String): UserDetails {
        return UsernamePasswordAuthenticationToken(email, password)
            .let { authenticationManager.authenticate(it) }
            .principal as UserDetails
    }

    private fun createAuthenticationTokens(userDetails: UserDetails): JwtCookies {
        return JwtCookies(
            authToken = jwtTokenService.generateAuthToken(userDetails),
            refreshToken = jwtTokenService.generateRefreshToken(userDetails),
            cookiesValidity = securityProperties.refreshTokenValidity,
            isSecure = serverProperties.ssl?.isEnabled ?: false
        )
    }
}
