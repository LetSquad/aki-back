package moscow.createdin.backend.model.entity

import org.postgresql.util.PGobject

data class AkiUserEntity(
    val id: Long?,

    val email: String,
    val password: String,
    val role: String,

    val specializations: PGobject,

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
