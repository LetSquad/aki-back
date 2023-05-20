package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.dto.AkiUserDTO
import moscow.createdin.backend.model.dto.UserRoleDTO
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class UserMapper {

    fun domainToDto(user: AkiUser) = AkiUserDTO(
        id = user.id,
        email = user.email,
        userRole = user.role,
        firstName = user.firstName,
        lastName = user.lastName,
        surname = user.surname,
        phone = user.phone,
        userImage = user.userImage,
        inn = user.inn,
        entityName = user.entityName,
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
}
