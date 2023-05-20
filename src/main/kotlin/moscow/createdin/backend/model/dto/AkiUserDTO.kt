package moscow.createdin.backend.model.dto

import moscow.createdin.backend.model.enums.UserRole

data class AkiUserDTO(
    val id: Long,

    val email: String,
    val userRole: UserRole,

    val firstName: String,
    val lastName: String,
    val surname: String?,
    val phone: String,

    val userImage: String?,
    val inn: String?,
    val entityName: String?,
    val jobTitle: String?
)
