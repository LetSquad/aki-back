package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.AkiUserEntity
import moscow.createdin.backend.repository.mapper.AkiUserRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AkiUserJdbc(
    private val jdbcTemplate: JdbcTemplate,
    private val rowMapper: AkiUserRowMapper
) : AkiUserRepository {

    override fun existsByEmail(email: String): Boolean {
        return jdbcTemplate.queryForObject(
            """
                SELECT COUNT(*) count
                FROM aki_user 
                WHERE email = ?
            """, { rs, _ -> rs.getInt("count") }, email
        )!! == 1
    }

    override fun existsByPhone(phone: String): Boolean {
        return jdbcTemplate.queryForObject(
            """
                SELECT COUNT(*) count
                FROM aki_user 
                WHERE phone = ?
            """, { rs, _ -> rs.getInt("count") }, phone
        )!! == 1
    }

    override fun findById(id: Long): AkiUserEntity {
        return jdbcTemplate.queryForObject(
            """
                SELECT id, email, id, email, password, role, first_name, last_name, middle_name, 
                    phone, user_image, inn, organization, job_title, is_activated, is_banned 
                FROM aki_user 
                WHERE id = ?
            """, rowMapper, id
        )!!
    }

    override fun findByEmail(email: String): AkiUserEntity {
        return jdbcTemplate.queryForObject(
            """
                SELECT id, email, id, email, password, role, first_name, last_name, middle_name, 
                    phone, user_image, inn, organization, job_title, is_activated, is_banned 
                FROM aki_user 
                WHERE email = ?
            """, rowMapper, email
        )!!
    }

    override fun save(user: AkiUserEntity) {
        jdbcTemplate.update(
            """
                INSERT INTO aki_user (email, password, role, first_name, last_name, middle_name, 
                    phone, user_image, inn, organization, job_title, is_activated, is_banned) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """,
            user.email, user.password, user.role, user.firstName, user.lastName, user.middleName,
            user.phone, user.userImage, user.inn, user.organization, user.jobTitle, user.isActivated, user.isBanned
        )
    }
}
