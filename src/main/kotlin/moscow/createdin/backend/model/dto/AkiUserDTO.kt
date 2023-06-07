package moscow.createdin.backend.model.dto

import moscow.createdin.backend.model.enums.UserRole
import moscow.createdin.backend.model.enums.UserSpecialization

data class AkiUserDTO(
    val id: Long,

    val email: String,
    val userRole: UserRole,

    val specializations: List<UserSpecialization>,

    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val phone: String,

    val userImage: String?,
    val inn: String?,
    val organization: String?,
    val organizationLogo: String?,
    val jobTitle: String?
)
