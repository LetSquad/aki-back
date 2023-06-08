package moscow.createdin.backend.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import moscow.createdin.backend.config.properties.AkiProperties
import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.dto.AkiUserDTO
import moscow.createdin.backend.model.dto.RegistrationRequestDTO
import moscow.createdin.backend.model.dto.UserRoleDTO
import moscow.createdin.backend.model.dto.place.PlaceUserDTO
import moscow.createdin.backend.model.entity.AkiUserEntity
import moscow.createdin.backend.model.enums.UserRole
import moscow.createdin.backend.model.enums.UserSpecialization
import moscow.createdin.backend.repository.mapper.toPGObject
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserMapper(
    private val properties: AkiProperties,
    private val gson: Gson,
    private val passwordEncoder: PasswordEncoder,
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
        isActivated = false,
        activationCode = UUID.randomUUID().toString(),
        isBanned = false,
        admin = null,
        banReason = null,
        specializations = registrationRequest.specializations.orEmpty()
    )

    fun domainToDto(user: AkiUser) = AkiUserDTO(
        id = user.id!!,
        email = user.email,
        userRole = user.role,
        specializations = user.specializations,
        firstName = user.firstName,
        lastName = user.lastName,
        middleName = user.middleName,
        phone = user.phone,
        userImage = if (!user.userImage.isNullOrBlank()) {
            "${properties.url}/${properties.imageUrlPrefix}/${user.userImage}"
        } else {
            null
        },
        inn = user.inn,
        organization = user.organization,
        organizationLogo = if (!user.logoImage.isNullOrBlank()) {
            "${properties.url}/${properties.imageUrlPrefix}/${user.logoImage}"
        } else {
            null
        },
        jobTitle = user.jobTitle
    )

    fun domainToPlaceDto(user: AkiUser) = PlaceUserDTO(
        id = user.id!!,
        email = user.email,
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
        specializations = gson.toJson(user.specializations).toPGObject(),
        banReason = user.banReason,
        admin = user.admin
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
        specializations = gson.fromJson<List<String>>(
            user.specializations.value,
            object : TypeToken<Collection<String>>() {}.type
        ).map { UserSpecialization.valueOf(it) },
        admin = user.admin,
        banReason = user.banReason
    )
}
