package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import moscow.createdin.backend.model.dto.AkiUserDTO
import moscow.createdin.backend.model.dto.AkiUserUpdateDTO
import moscow.createdin.backend.model.dto.ResetUserPasswordTO
import moscow.createdin.backend.service.ResetPasswordService
import moscow.createdin.backend.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val resetPasswordService: ResetPasswordService
) {

    @GetMapping
    fun getCurrentUser(): AkiUserDTO {
        return userService.getCurrentUser()
    }

    @PutMapping()
    fun edit(
        @Parameter(
            ref = "Модель данных юзера",
            schema = Schema(type = "string", format = "binary")
        ) @RequestPart user: AkiUserUpdateDTO,
        @Parameter(ref = "Фотография юзера") image: MultipartFile?
    ): AkiUserDTO {
        return userService.update(user, image)
    }

    @PostMapping("reset-password")
    @Operation(
        summary = "Восстановление пароля пользователя",
        description = "Отправляем email-сообщение с ссылкой на восстановление пароля пользователя"
    )
    fun resetUserPassword(@Parameter(ref = "Email пользователя") @RequestParam userEmail: String) {
        resetPasswordService.resetPassword(userEmail)
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
