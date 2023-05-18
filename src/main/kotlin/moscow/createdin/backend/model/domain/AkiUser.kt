package moscow.createdin.backend.model.domain

data class AkiUser(
    val id: Long,

    val email: String,
    val password: String,
    val role: String,

    val isActive: Boolean,
    val isBanned: Boolean
)
