package moscow.createdin.backend.model.entity

data class AkiUserEntity(
    val id: Long?,

    val email: String,
    val password: String,
    val role: String,

    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val phone: String,

    val userImage: String?,
    val inn: String?,
    val organization: String?,
    val jobTitle: String?,

    val isActive: Boolean,
    val isBanned: Boolean
)
