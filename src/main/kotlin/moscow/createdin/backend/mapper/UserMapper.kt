package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.dto.UserRoleDTO
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class UserMapper {

    fun detailsDomainToRoleDTO(userDetails: UserDetails) = UserRoleDTO(
        role = userDetails.authorities.first().authority
    )

    fun domainToDetailsDomain(user: AkiUser): UserDetails = User(
        user.email,
        user.password,
        user.isActive,
        true,
        true,
        !user.isBanned,
        setOf(SimpleGrantedAuthority(user.role))
    )
}
