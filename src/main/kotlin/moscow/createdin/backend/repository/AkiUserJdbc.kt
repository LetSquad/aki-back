package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.AkiUserEntity
import moscow.createdin.backend.repository.mapper.AkiUserRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository


@Repository
class AkiUserJdbc(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val rowMapper: AkiUserRowMapper
) : AkiUserRepository {

    override fun existsByEmail(email: String): Boolean {
        val parameters = MapSqlParameterSource()
            .addValue("email", email)
        return jdbcTemplate.queryForObject(
            """
                SELECT COUNT(*) count
                FROM aki_user 
                WHERE email = :email
            """, parameters
        ) { rs, _ -> rs.getInt("count") }!! == 1
    }

    override fun existsByPhone(phone: String): Boolean {
        val parameters = MapSqlParameterSource()
            .addValue("phone", phone)
        return jdbcTemplate.queryForObject(
            """
                SELECT COUNT(*) count
                FROM aki_user 
                WHERE phone = :phone
            """, parameters
        ) { rs, _ -> rs.getInt("count") }!! == 1
    }

    override fun findById(id: Long): AkiUserEntity {
        val parameters = MapSqlParameterSource()
            .addValue("id", id)
        return jdbcTemplate.queryForObject(
            """
                SELECT id, type, email, password, role, first_name, last_name, middle_name, 
                    phone, user_image, logo_image, inn, organization, job_title, is_activated, is_banned, admin_id, ban_reason
                FROM aki_user 
                WHERE id = :id
            """, parameters, rowMapper
        )!!
    }

    override fun findByEmail(email: String): AkiUserEntity {
        val parameters = MapSqlParameterSource()
            .addValue("email", email)
        return jdbcTemplate.queryForObject(
            """
                SELECT id, type, email, id, email, password, role, first_name, last_name, middle_name, 
                    phone, user_image, logo_image, inn, organization, job_title, is_activated, is_banned, admin_id, ban_reason
                FROM aki_user 
                WHERE email = :email
            """, parameters, rowMapper
        )!!
    }

    override fun save(user: AkiUserEntity) {
        val parameters = getNamedParameters(
            user.email,
            user.role,
            user.firstName,
            user.lastName,
            user.middleName,
            user.phone,
            user.inn,
            user.organization,
            user.jobTitle
        )
        parameters.addValue("password", user.password)
        parameters.addValue("userImage", user.userImage)
        parameters.addValue("logoImage", user.logoImage)
        parameters.addValue("isActivated", user.isActivated)
        parameters.addValue("isBanned", user.isBanned)
        parameters.addValue("banReason", user.banReason)
        parameters.addValue("type", user.type)
        parameters.addValue("adminId", user.admin?.id)
        jdbcTemplate.update(
            """
                INSERT INTO aki_user (email, type, password, role, first_name, last_name, middle_name, 
                    phone, user_image, logo_image, inn, organization, job_title, is_activated, is_banned, admin_id, ban_reason) 
                VALUES (:email, :type, :password, :role, :firstName, :lastName, :middleName, :phone, :userImage,  :logoImage, :inn, :organization, :jobTitle, :isActivated, :isBanned, :adminId, :banReason)
            """,
            parameters
        )
    }

    override fun findAll(
        email: String?, role: String?, firstName: String?, lastName: String?, middleName: String?,
        phone: String?, inn: String?, organization: String?, jobTitle: String?, offset: Long, limit: Int
    ): List<AkiUserEntity> {
        val query = """
            $SQL_SELECT_ENTITY
                WHERE (:email = '' OR email like :email)
                AND (:role = '' OR role like :role)
                AND (:firstName = '' OR first_name like :firstName)
                AND (:lastName = '' OR last_name like :lastName)
                AND (:middleName = '' OR middle_name like :middleName)
                AND (:phone = '' OR phone like :phone)
                AND (:inn = '' OR inn like :inn)
                AND (:organization = '' OR organization like :organization)
                AND (:jobTitle = '' OR job_title like :jobTitle)
                LIMIT :limit OFFSET :offset
        """
        val namedParameters =
            getNamedParameters(email, role, firstName, lastName, middleName, phone, inn, organization, jobTitle)
        namedParameters.addValue("limit", limit)
        namedParameters.addValue("offset", offset)
        return jdbcTemplate.query(
            query, namedParameters, rowMapper
        )
    }

    override fun countByFilter(
        email: String?, role: String?, firstName: String?, lastName: String?, middleName: String?,
        phone: String?, inn: String?, organization: String?, jobTitle: String?
    ): Int {
        val namedParameters =
            getNamedParameters(email, role, firstName, lastName, middleName, phone, inn, organization, jobTitle)
        return jdbcTemplate.queryForObject(
            """
                SELECT COUNT(*) as count
                FROM aki_user 
                WHERE (:email = '' OR email like :email)
                AND (:role = '' OR role = :role)
                AND (:firstName = '' OR first_name like :firstName)
                AND (:lastName = '' OR last_name like :lastName)
                AND (:middleName = '' OR middle_name like :middleName)
                AND (:phone = '' OR phone like :phone)
                AND (:inn = '' OR inn like :inn)
                AND (:organization = '' OR organization like :organization)
                AND (:jobTitle = '' OR job_title like :jobTitle)
            """, namedParameters
        ) { rs, _ -> rs.getInt("count") }!!
    }

    private fun getNamedParameters(
        email: String?, role: String?, firstName: String?, lastName: String?, middleName: String?,
        phone: String?, inn: String?, organization: String?, jobTitle: String?
    ): MapSqlParameterSource {
        val mapSqlParameterSource = MapSqlParameterSource()
        return mapSqlParameterSource
            .addValue("email", getLikeName(email.orEmpty()))
            .addValue("role", role.orEmpty())
            .addValue("firstName", getLikeName(firstName.orEmpty()))
            .addValue("lastName", getLikeName(lastName.orEmpty()))
            .addValue("middleName", getLikeName(middleName.orEmpty()))
            .addValue("phone", getLikeName(phone.orEmpty()))
            .addValue("inn", getLikeName(inn.orEmpty()))
            .addValue("organization", getLikeName(organization.orEmpty()))
            .addValue("jobTitle", getLikeName(jobTitle.orEmpty()))
    }

    private fun getLikeName(parameter: String?): String? {
        if (parameter.isNullOrEmpty()) {
            return parameter
        }
        return "$parameter"
    }

    companion object {
        private const val SQL_SELECT_ENTITY = "SELECT id, type, email, password, role, first_name, last_name, middle_name, " +
                "phone, user_image, logo_image, inn, organization, job_title, is_activated, is_banned, admin_id, ban_reason " +
                "FROM aki_user"
    }
}
