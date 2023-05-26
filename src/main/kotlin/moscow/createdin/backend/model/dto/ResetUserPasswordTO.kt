package moscow.createdin.backend.model.dto

data class ResetUserPasswordTO(
    val token: String,
    val password: String
)
