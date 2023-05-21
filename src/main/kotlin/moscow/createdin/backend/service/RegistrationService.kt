package moscow.createdin.backend.service

import moscow.createdin.backend.exception.UserAlreadyExistsException
import moscow.createdin.backend.mapper.UserMapper
import moscow.createdin.backend.model.dto.RegistrationRequestDTO
import org.springframework.stereotype.Service

@Service
class RegistrationService(
    private val userMapper: UserMapper,
    private val userService: UserService
) {

    fun registerUser(registrationRequest: RegistrationRequestDTO) {
        if (userService.isEmailExists(registrationRequest.email)) {
            throw UserAlreadyExistsException("email", registrationRequest.email)
        }
        if (userService.isPhoneExists(registrationRequest.phone)) {
            throw UserAlreadyExistsException("phone", registrationRequest.phone)
        }

        userMapper.registrationDtoToDomain(registrationRequest)
            .let { userService.registerUser(it) }
    }
}
