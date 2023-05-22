package moscow.createdin.backend.repository

interface UserRefreshTokenRepository {

    fun findByEmail(email: String): String

    fun save(email: String)

    fun update(email: String, refreshToken: String)
}
