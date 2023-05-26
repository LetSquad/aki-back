package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.UserPasswordResetTokenEntity

interface UserPasswordResetTokenRepository {

    fun save(entity: UserPasswordResetTokenEntity)
    fun findByToken(token: String): UserPasswordResetTokenEntity

}
