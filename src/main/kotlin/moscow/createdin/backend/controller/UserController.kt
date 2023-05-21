package moscow.createdin.backend.controller

import moscow.createdin.backend.model.dto.AkiUserDTO
import moscow.createdin.backend.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getCurrentUser(): AkiUserDTO {
        return userService.getCurrentUser()
    }
}
