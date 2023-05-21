package moscow.createdin.backend.model.dto

import moscow.createdin.backend.model.enums.UserRole

data class AkiUserDTO(
    val id: Long,

    val email: String,
    val userRole: UserRole,

    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val phone: String,

    val userImage: String?,
    val inn: String?,
    val organization: String?,
    val jobTitle: String?
)
