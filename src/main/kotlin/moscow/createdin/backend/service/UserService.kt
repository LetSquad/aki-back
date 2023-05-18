package moscow.createdin.backend.service

import moscow.createdin.backend.model.domain.AkiUser
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val passwordEncoder: PasswordEncoder) {

    fun getUser(email: String): AkiUser {
        return AkiUser(
            id = 123L,
            email = email,
            password = passwordEncoder.encode("qwerty"),
            role = "ADMIN",
            isActive = true,
            isBanned = false
        )
    }
}
