package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.UserPasswordResetTokenEntity
import moscow.createdin.backend.repository.mapper.UserPasswordResetTokenRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class UserPasswordResetTokenJdbc(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val rowMapper: UserPasswordResetTokenRowMapper
) : UserPasswordResetTokenRepository {

    override fun save(entity: UserPasswordResetTokenEntity) {
        val parameters = MapSqlParameterSource()
            .addValue("userId", entity.user.id)
            .addValue("resetToken", entity.resetToken)
            .addValue("expire", entity.expire)

        jdbcTemplate.update(
            """
                INSERT INTO user_password_reset_token (user_id, reset_token, expire) 
                VALUES (:userId, :resetToken, :expire)
            """,
            parameters
        )
    }

    override fun findByToken(token: String): UserPasswordResetTokenEntity {
        val parameters = MapSqlParameterSource()
            .addValue("token", token)

        return jdbcTemplate.queryForObject(
            """
                SELECT 
                    uprt.id,
                    uprt.user_id,
                    uprt.reset_token,
                    uprt.expire,
                    
                    u.id,
                    u.user_email,
                    u.password,
                    u.role,
                    u.user_type,
                    u.first_name,
                    u.last_name,
                    u.middle_name,
                    u.user_phone,
                    u.user_image,
                    u.inn,
                    u.organization,
                    u.activation_code,
                    u.logo_image,
                    u.job_title,
                    u.is_activated,
                    u.is_banned,
                    u.user_ban_reason,
                    u.user_admin_id
                FROM 
                    user_password_reset_token uprt
                INNER JOIN aki_user u on uprt.user_id = u.id
                WHERE 
                    uprt.reset_token = :token
            """, parameters, rowMapper
        )!!
    }
}
