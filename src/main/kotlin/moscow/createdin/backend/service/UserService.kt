package moscow.createdin.backend.service

import moscow.createdin.backend.context.UserContext
import moscow.createdin.backend.mapper.UserMapper
import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.dto.AkiUserDTO
import moscow.createdin.backend.repository.AkiUserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userContext: UserContext,
    private val userMapper: UserMapper,
    private val userRepository: AkiUserRepository
) {

    fun isEmailExists(email: String): Boolean = userRepository.existsByEmail(email)

    fun isPhoneExists(phone: String): Boolean = userRepository.existsByPhone(phone)

    fun getCurrentUser(): AkiUserDTO {
        return getUserByEmail(userContext.userEmail)
            .let { userMapper.domainToDto(it) }
    }

    fun getUserByEmail(email: String): AkiUser = userRepository.findByEmail(email)
        .let { userMapper.entityToDomain(it) }

    fun registerUser(user: AkiUser) {
        userMapper.domainToEntity(user)
            .also { userRepository.save(it) }
    }
}
