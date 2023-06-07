package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.AkiUserEntity
import moscow.createdin.backend.repository.mapper.AkiUserRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
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
                WHERE user_email = :email
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
                WHERE user_phone = :phone
            """, parameters
        ) { rs, _ -> rs.getInt("count") }!! == 1
    }

    override fun findById(id: Long): AkiUserEntity {
        val parameters = MapSqlParameterSource()
            .addValue("id", id)
        return jdbcTemplate.queryForObject(
            """
                SELECT id as user_id, specializations, user_email, password, role, first_name, last_name, middle_name, 
                    user_phone, user_image, logo_image, inn, organization, job_title, is_activated, 
                    activation_code, is_banned, user_admin_id, user_ban_reason
                FROM aki_user 
                WHERE id = :id
            """, parameters, rowMapper
        )!!
    }

    override fun findByEmail(email: String): AkiUserEntity {
        val parameters = MapSqlParameterSource()
        parameters.addValue("email", email)

        return jdbcTemplate.queryForObject(
            """
                SELECT 
                    id as user_id, 
                    specializations, 
                    user_email, 
                    password, 
                    role, 
                    first_name, 
                    last_name, 
                    middle_name, 
                    user_phone, 
                    user_image, 
                    logo_image, 
                    inn, 
                    organization, 
                    job_title, 
                    is_activated, 
                    activation_code, 
                    is_banned, 
                    user_admin_id, 
                    user_ban_reason
                FROM 
                    aki_user 
                WHERE 
                    user_email = :email
            """, parameters, rowMapper
        )!!
    }

    override fun save(user: AkiUserEntity) {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
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
        parameters.addValue("activationCode", user.activationCode)
        parameters.addValue("isBanned", user.isBanned)
        parameters.addValue("banReason", user.banReason)
        parameters.addValue("specializations", user.specializations)
        parameters.addValue("adminId", user.admin)
        jdbcTemplate.update(
            """
                INSERT INTO aki_user (user_email, specializations, password, role, first_name, last_name, middle_name, 
                    user_phone, user_image, logo_image, inn, organization, job_title, is_activated, activation_code, is_banned, user_admin_id, user_ban_reason) 
                VALUES (:email, :specializations, :password, :role, :firstName, :lastName, :middleName, :phone, :userImage,  :logoImage, :inn, :organization, :jobTitle, :isActivated, :activationCode, :isBanned, :adminId, :banReason)
            """,
            parameters, keyHolder
        )


    }

    override fun update(user: AkiUserEntity) {
        val parameters = MapSqlParameterSource()
            .addValue("id", user.id)
            .addValue("specializations", user.specializations)
            .addValue("firstName", user.firstName)
            .addValue("lastName", user.lastName)
            .addValue("middleName", user.middleName)
            .addValue("phone", user.phone)
            .addValue("userImage", user.userImage)
            .addValue("inn", user.inn)
            .addValue("organization", user.organization)
            .addValue("logoImage", user.logoImage)
            .addValue("jobTitle", user.jobTitle)
        jdbcTemplate.update(
            """
                UPDATE aki_user 
                SET specializations = :specializations, first_name = :firstName, last_name = :lastName, middle_name = :middleName, 
                    user_phone = :phone, user_image = :userImage, inn = :inn, organization = :organization,
                    logo_image = :logoImage, job_title = :jobTitle
                WHERE id = :id
            """,
            parameters
        )
    }

    override fun activateUser(activationCode: String): Boolean {
        val parameters = MapSqlParameterSource()
            .addValue("activationCode", activationCode)
        val updatedRows: Int = jdbcTemplate.update(
            """
                UPDATE aki_user SET is_activated = true, activation_code = NULL 
                WHERE activation_code = :activationCode
            """,
            parameters
        )
        return updatedRows == 1
    }

    override fun findAll(
        email: String?, role: String?, firstName: String?, lastName: String?, middleName: String?,
        phone: String?, inn: String?, organization: String?, jobTitle: String?, offset: Long, limit: Int
    ): List<AkiUserEntity> {
        val query = """
            $SQL_SELECT_ENTITY
                WHERE (:email = '' OR user_email like :email)
                AND (:role = '' OR role like :role)
                AND (:firstName = '' OR first_name like :firstName)
                AND (:lastName = '' OR last_name like :lastName)
                AND (:middleName = '' OR middle_name like :middleName)
                AND (:phone = '' OR user_phone like :phone)
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
                WHERE (:email = '' OR user_email like :email)
                AND (:role = '' OR role = :role)
                AND (:firstName = '' OR first_name like :firstName)
                AND (:lastName = '' OR last_name like :lastName)
                AND (:middleName = '' OR middle_name like :middleName)
                AND (:phone = '' OR user_phone like :phone)
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
        private const val SQL_SELECT_ENTITY = "SELECT id as user_id, specializations, user_email, password, role, first_name, last_name, middle_name, " +
                "user_phone, user_image, logo_image, inn, organization, job_title, is_activated, activation_code, is_banned, user_admin_id, user_ban_reason " +
                "FROM aki_user"
    }
}
