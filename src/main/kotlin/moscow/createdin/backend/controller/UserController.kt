package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
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

@Tag(name = "Методы работы с пользователем")
@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val resetPasswordService: ResetPasswordService
) {

    @Operation(
        summary = "Бан пользователя администратором"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("ban")
    fun ban(@RequestBody banRequestDTO: BanRequestDTO): AkiUserDTO {
        return userService.ban(banRequestDTO)
    }

    @Operation(
        summary = "Получение данных текущего пользователя по токену"
    )
    @GetMapping
    fun getCurrentUser(): AkiUserDTO {
        return userService.getCurrentUser()
    }

    @Operation(
        summary = "Изменение данных пользователя"
    )
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


    @Operation(
        summary = "Подтверждение зарегистрированного пользователя",
        description = "После перехода по ссылке пользователь становится активным"
    )
    @GetMapping("activate/{code}")
    fun activate(
        @Parameter(ref = "Пользовательский код подтверждения") @PathVariable code: String,
        response: HttpServletResponse
    ) {
        userService.activateUser(code)
        response.sendRedirect("/?activation=true")
    }

    @Operation(
        summary = "Восстановление пароля пользователя",
        description = "Отправляем email-сообщение с ссылкой на восстановление пароля пользователя"
    )
    @PostMapping("reset-password")
    fun resetUserPassword(@Parameter(ref = "Email пользователя") @RequestBody req: ResetPasswordAkiUserDTOList) {
        resetPasswordService.resetPassword(req.userEmail)
    }


    @Operation(summary = "Валидация токена восстановления пароля пользователя")
    @GetMapping("validate-reset-password-token/{token}")
    fun validateUserResetPasswordToken(
        @Parameter(ref = "Токен восстановления пароля пользователя") @PathVariable token: String
    ): Boolean {
        return resetPasswordService.isTokenValid(token)
    }


    @Operation(summary = "Изменение пароля пользователя (через процедуру восстановления пароля)")
    @PostMapping("change-password")
    fun changeUserPassword(
        @Parameter(ref = "Модель данных обновления пароля пользователя") @RequestBody resetUserPasswordTO: ResetUserPasswordTO
    ) {
        resetPasswordService.changePassword(resetUserPasswordTO)
    }
}
