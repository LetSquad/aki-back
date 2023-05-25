package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.AreaEntity
import moscow.createdin.backend.repository.mapper.AreaRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository


@Repository
class AreaJdbc(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val rowMapper: AreaRowMapper
) : AreaRepository {

    override fun findById(id: Long): AreaEntity {
        val parameters = MapSqlParameterSource()
            .addValue("id", id)
        return jdbcTemplate.queryForObject(
            """
                $SQL_SELECT_ENTITY
                WHERE area.id = :id
            """, parameters, rowMapper
        )!!
    }

    override fun save(area: AreaEntity): Long {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        val parameters = MapSqlParameterSource()
        parameters.addValue("userId", area.user.id)
        parameters.addValue("name", area.name)
        parameters.addValue("description", area.description)
        parameters.addValue("areaImage", getLikeName(area.areaImage.orEmpty()))
        parameters.addValue("status", area.status)
        parameters.addValue("address", area.address)
        parameters.addValue("website", getLikeName(area.website.orEmpty()))
        parameters.addValue("email", getLikeName(area.email.orEmpty()))
        parameters.addValue("phone", getLikeName(area.phone.orEmpty()))
//        parameters.addValue("coordinatesId", area.coordinates) // TODO
        parameters.addValue("banReason", getLikeName(area.banReason.orEmpty()))
        parameters.addValue("adminId", area.admin?.id)

        jdbcTemplate.update(
            """
                INSERT INTO area (user_id, name, description, area_image, status, address, website, email, phone, ban_reason, admin_id) 
                VALUES (:userId, :name, :description, :areaImage, :status, :address, :website, :email, :phone, :banReason, :adminId)
            """,
            parameters, keyHolder, arrayOf("id")
        )

        return keyHolder.key?.toLong() ?: -1
    }

    override fun update(area: AreaEntity) {
        val parameters = MapSqlParameterSource()
            .addValue("id", area.id)
            .addValue("status", area.status)
            .addValue("name", area.name)
            .addValue("description", area.description)
            .addValue("areaImage", area.areaImage)
            .addValue("address", area.address)
            .addValue("website", area.website)
            .addValue("email", area.email)
            .addValue("phone", area.phone)
            .addValue("banReason", area.banReason)
            .addValue("adminId", area.admin?.id)
//            .addValue("coordinates", area.coordinates) // TODO
        jdbcTemplate.update(
            """
                UPDATE area 
                SET name = :name, description = :description, area_image = :areaImage, status = :status, 
                    address = :address, website = :website, email = :email, phone = :phone,
                    ban_reason = :banReason, admin_id = :adminId
                WHERE area.id = :id
            """,
            parameters
        )
    }

    override fun findByUserId(userId: Long): List<AreaEntity> {
        val parameters = MapSqlParameterSource()
            .addValue("userId", userId)
        return jdbcTemplate.query(
            """
                $SQL_SELECT_ENTITY
                WHERE area.user_id = :userId
            """, parameters, rowMapper
        )
    }

    private fun getLikeName(parameter: String?): String? {
        if (parameter.isNullOrEmpty()) {
            return parameter
        }
        return "$parameter"
    }

    companion object {
        private const val SQL_SELECT_ENTITY =
            """
                SELECT area.id,
                       area.name,
                       area.description,
                       area.area_image,
                       area.address,
                       area.website,
                       area.email,
                       area.phone,
                       area.status,
                       area.ban_reason,
                       area.admin_id,
                       area.coordinates_id,
                
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
                       aki_user.type,
                
                       area_admin.id,
                       area_admin.email,
                       area_admin.password,
                       area_admin.role,
                       area_admin.first_name,
                       area_admin.last_name,
                       area_admin.middle_name,
                       area_admin.phone,
                       area_admin.user_image,
                       area_admin.inn,
                       area_admin.organization,
                       area_admin.logo_image,
                       area_admin.job_title,
                       area_admin.is_activated,
                       area_admin.activation_code,
                       area_admin.is_banned,
                       area_admin.admin_id,
                       area_admin.ban_reason,
                       area_admin.type
                FROM area
                         INNER JOIN aki_user on area.user_id = aki_user.id
                         FULL JOIN aki_user area_admin on area.admin_id = area_admin.id
            """
    }
}
