package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import moscow.createdin.backend.model.dto.RegistrationRequestDTO
import moscow.createdin.backend.service.RegistrationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Методы регистрации")
@RestController
@RequestMapping("/api/register")
class RegistrationController(private val registrationService: RegistrationService) {

    @Operation(
        summary = "Регистрация пользователя",
        description = "Регистрация пользователя"
    )
    @PostMapping
    fun postRegistration(registrationRequest: RegistrationRequestDTO) {
        registrationService.registerUser(registrationRequest)
    }
}
