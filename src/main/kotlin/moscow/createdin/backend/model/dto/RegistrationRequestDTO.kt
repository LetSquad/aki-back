package moscow.createdin.backend.model.dto

import moscow.createdin.backend.model.enums.UserSpecialization

data class RegistrationRequestDTO(
    val email: String,
    val password: String,

    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val phone: String,

    val specializations: List<UserSpecialization>?,

    val inn: String?,
    val jobTitle: String?,
    val organization: String?,

    val role: String
)
