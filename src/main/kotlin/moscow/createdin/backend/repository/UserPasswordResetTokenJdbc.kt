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
                    
                    aki_user.id,
                    aki_user.email,
                    aki_user.password,
                    aki_user.role,
                    aki_user.first_name,
                    aki_user.last_name,
                    aki_user.middle_name,
                    aki_user.phone,
                    aki_user.user_image,
                    aki_user.inn,
                    aki_user.organization,
                    aki_user.logo_image,
                    aki_user.job_title,
                    aki_user.is_activated,
                    aki_user.activation_code,
                    aki_user.is_banned,
                    aki_user.admin_id,
                    aki_user.ban_reason,
                    aki_user.type
                FROM user_password_reset_token uprt
                INNER JOIN aki_user on uprt.user_id = aki_user.id
                WHERE uprt.reset_token = :token
            """, parameters, rowMapper
        )!!
    }
}
