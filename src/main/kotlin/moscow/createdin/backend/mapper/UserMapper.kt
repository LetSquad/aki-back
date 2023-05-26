package moscow.createdin.backend.mapper

import moscow.createdin.backend.config.properties.AkiProperties
import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.dto.AkiUserDTO
import moscow.createdin.backend.model.dto.RegistrationRequestDTO
import moscow.createdin.backend.model.dto.UserRoleDTO
import moscow.createdin.backend.model.entity.AkiUserEntity
import moscow.createdin.backend.model.enums.UserRole
import moscow.createdin.backend.model.enums.UserType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserMapper(
    private val akiProperties: AkiProperties,
    private val passwordEncoder: PasswordEncoder,
    private val akiUserAdminMapper: AkiUserAdminMapper
) {

    fun registrationDtoToDomain(registrationRequest: RegistrationRequestDTO) = AkiUser(
        id = null,
        email = registrationRequest.email.lowercase(),
        password = passwordEncoder.encode(registrationRequest.password),
        role = UserRole.valueOf(registrationRequest.role),
        firstName = registrationRequest.firstName,
        lastName = registrationRequest.lastName,
        middleName = registrationRequest.middleName,
        phone = registrationRequest.phone,
        userImage = null,
        inn = registrationRequest.inn,
        organization = registrationRequest.organization,
        logoImage = null,
        jobTitle = registrationRequest.jobTitle,
        isActivated = true, // TODO прикрутить подтверждение через почту
        activationCode = UUID.randomUUID().toString(),
        isBanned = false,
        admin = null,
        banReason = null,
        type = null // TODO добавить на фронте выбор типа юзера для системы рекомендаций
    )

    fun domainToDto(user: AkiUser) = AkiUserDTO(
        id = user.id!!,
        email = user.email,
        userRole = user.role,
        firstName = user.firstName,
        lastName = user.lastName,
        middleName = user.middleName,
        phone = user.phone,
        userImage =
        if (!user.userImage.isNullOrEmpty())
            "${akiProperties.url}/${akiProperties.imageUrlPrefix}/${user.userImage}" else null,
        inn = user.inn,
        organization = user.organization,
        jobTitle = user.jobTitle
    )

    fun detailsDomainToRoleDto(userDetails: UserDetails) = UserRoleDTO(
        role = userDetails.authorities.first().authority
    )

    fun domainToDetailsDomain(user: AkiUser): UserDetails = User(
        user.email,
        user.password,
        user.isActivated,
        true,
        true,
        !user.isBanned,
        setOf(SimpleGrantedAuthority(user.role.name))
    )

    fun domainToEntity(user: AkiUser) = AkiUserEntity(
        id = user.id,
        email = user.email,
        password = user.password,
        role = user.role.name,
        firstName = user.firstName,
        lastName = user.lastName,
        middleName = user.middleName,
        phone = user.phone,
        userImage = user.userImage,
        inn = user.inn,
        organization = user.organization,
        logoImage = user.logoImage,
        jobTitle = user.jobTitle,
        isActivated = user.isActivated,
        activationCode = user.activationCode,
        isBanned = user.isBanned,
        type = user.type?.name,
        banReason = user.banReason,
        admin = user.admin?.let { akiUserAdminMapper.domainToEntity(user.admin) }
    )

    fun entityToDomain(user: AkiUserEntity) = AkiUser(
        id = user.id,
        email = user.email,
        password = user.password,
        role = UserRole.valueOf(user.role),
        firstName = user.firstName,
        lastName = user.lastName,
        middleName = user.middleName,
        phone = user.phone,
        userImage = user.userImage,
        inn = user.inn,
        organization = user.organization,
        logoImage = user.logoImage,
        jobTitle = user.jobTitle,
        isActivated = user.isActivated,
        activationCode = user.activationCode,
        isBanned = user.isBanned,
        type = user.type?.let { UserType.valueOf(user.type) },
        admin = user.admin?.let { akiUserAdminMapper.entityToDomain(user.admin) },
        banReason = user.banReason
    )
}
