package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import moscow.createdin.backend.getLogger
import moscow.createdin.backend.model.cookie.CookieName
import moscow.createdin.backend.model.dto.SignInRequestDTO
import moscow.createdin.backend.model.dto.UserRoleDTO
import moscow.createdin.backend.service.auth.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@Tag(name = "Методы аутентификации")
@RestController
@RequestMapping("/api/auth")
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @Operation(
        summary = "Аутентификация пользователя",
        description = "В куки проставляются auth и refresh токены для пользователя"
    )
    @PostMapping
    fun postAuth(
        @RequestBody signIn: SignInRequestDTO,
        response: HttpServletResponse
    ): ResponseEntity<UserRoleDTO> = try {
        val (userRole, jwtCookies) = authenticationService.authUser(signIn)
        response.addCookie(jwtCookies.retrieveAuthCookie())
        response.addCookie(jwtCookies.retrieveRefreshCookie())

        ResponseEntity.ok(userRole)
    } catch (e: Exception) {
        log.warn("Unauthorized auth request", e)

        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .build()
    }

    @Operation(
        summary = "Обновление токенов пользователя",
        description = "В куки проставляются новые auth и refresh токены для пользователя"
    )
    @PostMapping("/refresh")
    fun postAuthRefresh(
        @Parameter(hidden = true) @CookieValue(CookieName.AUTH_TOKEN) authToken: String,
        @Parameter(hidden = true) @CookieValue(CookieName.REFRESH_TOKEN) refreshToken: String,
        response: HttpServletResponse
    ): ResponseEntity<UserRoleDTO> = try {
        val (userRole, jwtCookies) = authenticationService.updateTokens(authToken, refreshToken)
        response.addCookie(jwtCookies.retrieveAuthCookie())
        response.addCookie(jwtCookies.retrieveRefreshCookie())

        ResponseEntity.ok(userRole)
    } catch (e: Exception) {
        log.warn("Unauthorized token refresh request", e)

        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .build()
    }

    companion object {
        private val log = getLogger<AuthenticationController>()
    }
}
