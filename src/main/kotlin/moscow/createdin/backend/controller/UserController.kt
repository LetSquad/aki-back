package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import moscow.createdin.backend.model.dto.AkiUserDTO
import moscow.createdin.backend.model.dto.AkiUserUpdateDTO
import moscow.createdin.backend.model.dto.BanRequestDTO
import moscow.createdin.backend.model.dto.ResetPasswordAkiUserDTOList
import moscow.createdin.backend.model.dto.ResetUserPasswordTO
import moscow.createdin.backend.service.ResetPasswordService
import moscow.createdin.backend.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val resetPasswordService: ResetPasswordService
) {

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("ban")
    fun ban(@RequestBody banRequestDTO: BanRequestDTO): AkiUserDTO {
        return userService.ban(banRequestDTO)
    }

    @GetMapping
    fun getCurrentUser(): AkiUserDTO {
        return userService.getCurrentUser()
    }

    @PutMapping
    fun edit(
        @Parameter(
            ref = "Модель данных юзера",
            schema = Schema(type = "string", format = "binary")
        ) @RequestPart user: AkiUserUpdateDTO,
        @Parameter(ref = "Фотография юзера") image: MultipartFile?
    ): AkiUserDTO {
        return userService.update(user, image)
    }

    @GetMapping("activate/{code}")
    @Operation(
        summary = "Подтверждение зарегистрированного пользователя",
        description = "После перехода по ссылке пользователь становится активным"
    )
    fun activate(
        @Parameter(ref = "Пользовательский код подтверждения") @PathVariable code: String,
        response: HttpServletResponse
    ) {
        userService.activateUser(code)
        response.sendRedirect("/?activation=true")
    }

    @PostMapping("reset-password")
    @Operation(
        summary = "Восстановление пароля пользователя",
        description = "Отправляем email-сообщение с ссылкой на восстановление пароля пользователя"
    )
    fun resetUserPassword(@Parameter(ref = "Email пользователя") @RequestBody req: ResetPasswordAkiUserDTOList) {
        resetPasswordService.resetPassword(req.userEmail)
    }

    @GetMapping("validate-reset-password-token/{token}")
    @Operation(summary = "Валидация токена восстановления пароля пользователя")
    fun validateUserResetPasswordToken(
        @Parameter(ref = "Токен восстановления пароля пользователя") @PathVariable token: String): Boolean {
        return resetPasswordService.isTokenValid(token)
    }

    @PostMapping("change-password")
    @Operation(summary = "Изменение пароля пользователя (через процедуру восстановления пароля)")
    fun changeUserPassword(
        @Parameter(ref = "Модель данных обновления пароля пользователя") @RequestBody resetUserPasswordTO: ResetUserPasswordTO
    ) {
        resetPasswordService.changePassword(resetUserPasswordTO)
    }
}
