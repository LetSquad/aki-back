package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import moscow.createdin.backend.model.dto.AkiUserDTOList
import moscow.createdin.backend.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Методы работы админа")
@RestController
@RequestMapping("/api/admin")
class AdminController(private val userService: UserService) {

    @Operation(
        summary = "Получение пользователей по фильтрам"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("users")
    fun getUsers(
        @Parameter(description = "Почта пользователя") @RequestParam email: String?,
        @Parameter(description = "Роль пользователя") @RequestParam role: String?,
        @Parameter(description = "Имя пользователя") @RequestParam firstName: String?,
        @Parameter(description = "Фамилия пользователя") @RequestParam lastName: String?,
        @Parameter(description = "Отчество пользователя") @RequestParam middleName: String?,
        @Parameter(description = "Телефон пользователя") @RequestParam phone: String?,
        @Parameter(description = "Инн пользователя") @RequestParam inn: String?,
        @Parameter(description = "Организация пользователя") @RequestParam organization: String?,
        @Parameter(description = "Должность пользователя") @RequestParam jobTitle: String?,
        @Parameter(description = "Страница на фронте") @RequestParam pageNumber: Long,
        @Parameter(description = "Количество пользователей на страницу") @RequestParam limit: Int,
    ): AkiUserDTOList {
        return userService.getUsers(
            email = email,
            role = role,
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            phone = phone,
            inn = inn,
            organization = organization,
            jobTitle = jobTitle,
            pageNumber = pageNumber,
            limit = limit
        )
    }
}
