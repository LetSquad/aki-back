package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.dto.AkiUserDTO
import moscow.createdin.backend.model.dto.RegistrationRequestDTO
import moscow.createdin.backend.model.dto.UserRoleDTO
import moscow.createdin.backend.model.entity.AkiUserEntity
import moscow.createdin.backend.model.enums.UserRole
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserMapper(private val passwordEncoder: PasswordEncoder) {

    fun registrationDtoToDomain(registrationRequest: RegistrationRequestDTO) = AkiUser(
        id = null,
        email = registrationRequest.email.lowercase(),
        password = passwordEncoder.encode(registrationRequest.password),
        role = UserRole.RENTER,
        firstName = registrationRequest.firstName,
        lastName = registrationRequest.lastName,
        middleName = registrationRequest.middleName,
        phone = registrationRequest.phone,
        userImage = null,
        inn = null,
        organization = null,
        jobTitle = null,
        isActive = true,
        isBanned = false
    )

    fun domainToDto(user: AkiUser) = AkiUserDTO(
        id = user.id!!,
        email = user.email,
        userRole = user.role,
        firstName = user.firstName,
        lastName = user.lastName,
        middleName = user.middleName,
        phone = user.phone,
        userImage = user.userImage,
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
        user.isActive,
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
        jobTitle = user.jobTitle,
        isActive = user.isActive,
        isBanned = user.isBanned
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
        jobTitle = user.jobTitle,
        isActive = user.isActive,
        isBanned = user.isBanned
    )
}
