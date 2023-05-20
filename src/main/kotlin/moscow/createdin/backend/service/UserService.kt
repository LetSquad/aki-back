package moscow.createdin.backend.service

import moscow.createdin.backend.context.UserContext
import moscow.createdin.backend.mapper.UserMapper
import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.dto.AkiUserDTO
import moscow.createdin.backend.model.enums.UserRole
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userContext: UserContext,
    private val passwordEncoder: PasswordEncoder,
    private val userMapper: UserMapper
) {

    fun getCurrentUser(): AkiUserDTO {
        return getUserByEmail(userContext.userEmail)
            .let { userMapper.domainToDto(it) }
    }

    fun getUserByEmail(email: String): AkiUser {
        return AkiUser(
            id = 123L,
            email = email,
            password = passwordEncoder.encode("qwerty"),
            role = UserRole.ADMIN,
            firstName = "Иван",
            lastName = "Иванов",
            surname = "Иванович",
            phone = "+71234567890",
            userImage = null,
            inn = "123456789",
            entityName = null,
            jobTitle = null,
            isActive = true,
            isBanned = false
        )
    }
}
