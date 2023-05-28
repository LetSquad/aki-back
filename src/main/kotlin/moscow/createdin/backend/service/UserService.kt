package moscow.createdin.backend.service

import moscow.createdin.backend.context.UserContext
import moscow.createdin.backend.exception.ImageNotSavedException
import moscow.createdin.backend.exception.IncorrectActivationCodeException
import moscow.createdin.backend.getLogger
import moscow.createdin.backend.mapper.UserMapper
import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.dto.AkiUserDTO
import moscow.createdin.backend.model.dto.AkiUserDTOList
import moscow.createdin.backend.model.dto.AkiUserUpdateDTO
import moscow.createdin.backend.model.dto.BanRequestDTO
import moscow.createdin.backend.model.entity.AkiUserEntity
import moscow.createdin.backend.repository.AkiUserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class UserService(
    private val passwordEncoder: PasswordEncoder,
    private val filesystemService: FilesystemService,
    private val userContext: UserContext,
    private val userMapper: UserMapper,
    private val userRepository: AkiUserRepository
) {

    fun ban(banRequestDTO: BanRequestDTO): AkiUserDTO {
        val adminUser = getCurrentUserDomain()
        val editable = userRepository.findById(banRequestDTO.id)
            .let { userMapper.entityToDomain(it) }

        val result = editable.copy(
            isBanned = true,
            banReason = banRequestDTO.banReason,
            admin = adminUser.id
        )

        return userMapper.domainToEntity(result)
            .also { userRepository.update(it) }
            .let { userRepository.findById(result.id!!) }
            .let { userMapper.entityToDomain(it) }
            .let { userMapper.domainToDto(it) }
    }

    fun isEmailExists(email: String): Boolean = userRepository.existsByEmail(email)

    fun isPhoneExists(phone: String): Boolean = userRepository.existsByPhone(phone)

    fun getCurrentUser(): AkiUserDTO {
        return getUserByEmail(userContext.userEmail)
            .let { userMapper.domainToDto(it) }
    }

    fun update(
        req: AkiUserUpdateDTO,
        image: MultipartFile?
    ): AkiUserDTO {
        val userWithOldFields = userRepository.findById(req.id)
            .let { userMapper.entityToDomain(it) }
        var imageName: String? = null

        if (image != null) {
            try {
                imageName = filesystemService.saveImage(image)
            } catch (e: Exception) {
                log.error("Exception while saving image for user with id = " + req.id, e)
                throw ImageNotSavedException(req.id, e)
            }
        }

        val userWithNewFields = userWithOldFields.copy(
//            email = req.email ?: updatable.email, // TODO нужно обновлять также ключ в таблице рефреш токена
            firstName = req.firstName ?: userWithOldFields.firstName,
            lastName = req.lastName ?: userWithOldFields.lastName,
            middleName = req.middleName ?: userWithOldFields.middleName,
            phone = req.phone ?: userWithOldFields.phone,
            userImage = imageName ?: userWithOldFields.userImage,
            inn = req.inn ?: userWithOldFields.inn,
            organization = req.organization ?: userWithOldFields.organization,
            jobTitle = req.jobTitle ?: userWithOldFields.jobTitle,
        )

        userMapper.domainToEntity(userWithNewFields)
            .let { userRepository.update(it) }

        return userRepository.findById(req.id)
            .let { userMapper.entityToDomain(it) }
            .let { userMapper.domainToDto(it) }
    }

    fun activateUser(activationCode: String) {
        val isUserActivated: Boolean = userRepository.activateUser(activationCode)
        if (!isUserActivated) throw IncorrectActivationCodeException(activationCode)
    }

    fun getCurrentUserDomain(): AkiUser {
        return getUserByEmail(userContext.userEmail)
    }

    fun getUserByEmail(email: String): AkiUser = userRepository.findByEmail(email)
        .let { userMapper.entityToDomain(it) }

    fun getUserById(id: Long): AkiUserEntity = userRepository.findById(id)

    fun createUser(user: AkiUser) {
        userMapper.domainToEntity(user)
            .also { userRepository.save(it) }
    }

    fun getUsers(
        email: String?, role: String?, firstName: String?, lastName: String?, middleName: String?,
        phone: String?, inn: String?, organization: String?, jobTitle: String?, pageNumber: Long, limit: Int
    ): AkiUserDTOList {
        val count: Int = userRepository.countByFilter(
            email,
            role,
            firstName,
            lastName,
            middleName,
            phone,
            inn,
            organization,
            jobTitle
        )
        val users = userRepository.findAll(
            email,
            role,
            firstName,
            lastName,
            middleName,
            phone,
            inn,
            organization,
            jobTitle,
            (pageNumber - 1) * limit,
            limit
        )
            .map { userMapper.entityToDomain(it) }
            .map { userMapper.domainToDto(it) }
        return AkiUserDTOList(users, count)
    }

    fun changePassword(user: AkiUserEntity, newPassword: String) {
        val result = user.copy(
            jobTitle = passwordEncoder.encode(newPassword),
        )

        userRepository.save(result)
    }

    companion object {
        private val log = getLogger<UserService>()
    }
}
