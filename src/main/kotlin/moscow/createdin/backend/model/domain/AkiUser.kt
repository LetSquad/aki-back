package moscow.createdin.backend.model.domain

import moscow.createdin.backend.model.enums.UserRole
import moscow.createdin.backend.model.enums.UserType

data class AkiUser(
    val id: Long?,

    val email: String,
    val password: String,
    val role: UserRole,

    val type: UserType?,

    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val phone: String,

    val userImage: String?,
    val inn: String?,
    val organization: String?,
    val logoImage: String?,
    val jobTitle: String?,

    val isActivated: Boolean,
    val activationCode: String?,

    val isBanned: Boolean,
    val banReason: String?,
    val admin: Long?
)
