package moscow.createdin.backend.model.entity

import java.time.Instant

data class UserPasswordResetTokenEntity(
    val id: Long?,

    val user: AkiUserEntity,
    val resetToken: String,
    val expire: Instant
)
