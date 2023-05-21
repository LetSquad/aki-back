package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.AkiUserEntity

interface AkiUserRepository {

    fun existsByEmail(email: String): Boolean

    fun existsByPhone(phone: String): Boolean

    fun findById(id: Long): AkiUserEntity

    fun findByEmail(email: String): AkiUserEntity

    fun save(user: AkiUserEntity)
}
