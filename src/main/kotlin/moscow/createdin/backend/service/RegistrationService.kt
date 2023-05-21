package moscow.createdin.backend.service

import moscow.createdin.backend.exception.UserAlreadyExistsException
import moscow.createdin.backend.mapper.UserMapper
import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.dto.RegistrationRequestDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegistrationService(
    private val userMapper: UserMapper,
    private val userService: UserService,
    private val refreshTokenService: RefreshTokenService
) {

    @Transactional
    fun registerUser(registrationRequest: RegistrationRequestDTO) {
        if (userService.isEmailExists(registrationRequest.email)) {
            throw UserAlreadyExistsException("email", registrationRequest.email)
        }
        if (userService.isPhoneExists(registrationRequest.phone)) {
            throw UserAlreadyExistsException("phone", registrationRequest.phone)
        }

        val user: AkiUser = userMapper.registrationDtoToDomain(registrationRequest)
        userService.createUser(user)
        refreshTokenService.initUser(user.email)
    }
}
