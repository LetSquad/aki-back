package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.AkiUserEntity

interface AkiUserRepository {

    fun existsByEmail(email: String): Boolean

    fun existsByPhone(phone: String): Boolean

    fun findById(id: Long): AkiUserEntity

    fun findByEmail(email: String): AkiUserEntity

    fun save(user: AkiUserEntity)

    fun update(user: AkiUserEntity)

    fun activateUser(activationCode: String): Boolean

    fun findAll(
        email: String?, role: String?, firstName: String?, lastName: String?, middleName: String?,
        phone: String?, inn: String?, organization: String?, jobTitle: String?, offset: Long, limit: Int
    ): List<AkiUserEntity>

    fun countByFilter(
        email: String?, role: String?, firstName: String?, lastName: String?, middleName: String?,
        phone: String?, inn: String?, organization: String?, jobTitle: String?
    ): Int
}
