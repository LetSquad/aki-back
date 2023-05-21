package moscow.createdin.backend.model.dto

data class RegistrationRequestDTO(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val surname: String?,
    val phone: String
)
