package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.entity.AkiUserEntity
import moscow.createdin.backend.model.enums.UserRole
import moscow.createdin.backend.model.enums.UserType
import org.springframework.stereotype.Component

@Component
class AkiUserAdminMapper {

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
        inn = null,
        organization = null,
        logoImage = null,
        jobTitle = user.jobTitle,
        isActivated = user.isActivated,
        isBanned = user.isBanned,
        type = UserRole.ADMIN.name,
        banReason = user.banReason,
        admin = null,
        area = null,
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
        inn = null,
        organization = null,
        logoImage = null,
        jobTitle = user.jobTitle,
        isActivated = user.isActivated,
        isBanned = user.isBanned,
        type = user.type?.let { UserType.valueOf(user.type) },
        admin = null,
        area = null,
        banReason = user.banReason,
    )
}
