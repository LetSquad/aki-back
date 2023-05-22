package moscow.createdin.backend.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class UserRefreshTokenJdbc(private val jdbcTemplate: JdbcTemplate) : UserRefreshTokenRepository {

    override fun findByEmail(email: String): String {
        return jdbcTemplate.queryForObject(
            """
                SELECT refresh_token FROM user_refresh_token WHERE user_email = ?
            """, { rs, _ -> rs.getString("refresh_token") }, email
        )!!
    }

    override fun save(email: String) {
        jdbcTemplate.update("INSERT INTO user_refresh_token (user_email) VALUES (?)", email)
    }

    override fun update(email: String, refreshToken: String) {
        jdbcTemplate.update("UPDATE user_refresh_token SET refresh_token = ? WHERE user_email = ?", refreshToken, email)
    }
}
