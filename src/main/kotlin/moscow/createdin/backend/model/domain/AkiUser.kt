package moscow.createdin.backend.model.domain

import moscow.createdin.backend.model.enums.UserRole

data class AkiUser(
    val id: Long?,

    val email: String,
    val password: String,
    val role: UserRole,

    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val phone: String,

    val userImage: String?,
    val inn: String?,
    val organization: String?,
    val jobTitle: String?,

    val isActivated: Boolean,
    val isBanned: Boolean
)
