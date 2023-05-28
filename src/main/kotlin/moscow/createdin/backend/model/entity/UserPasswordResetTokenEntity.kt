package moscow.createdin.backend.model.entity

import java.sql.Timestamp

data class UserPasswordResetTokenEntity(
    val id: Long?,

    val user: AkiUserEntity,
    val resetToken: String,
    val expire: Timestamp
)
