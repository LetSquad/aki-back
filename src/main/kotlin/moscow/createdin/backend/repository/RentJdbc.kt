package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.PlaceEntity
import moscow.createdin.backend.model.entity.RentEntity
import moscow.createdin.backend.model.enums.AdminStatusType
import moscow.createdin.backend.model.enums.RentConfirmationStatus
import moscow.createdin.backend.repository.mapper.RentRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository


@Repository
class RentJdbc(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val rowMapper: RentRowMapper
) : RentRepository {

    override fun findById(id: Long): RentEntity {
        val parameters = MapSqlParameterSource()
            .addValue("id", id)
        return jdbcTemplate.queryForObject(
            """
                $SQL_SELECT_ENTITY
                WHERE
                    r.id = :id
                GROUP BY r.id, p.id, u.id, up.id
            """, parameters, rowMapper
        )!!
    }

    override fun update(rent: RentEntity) {
        val parameters = MapSqlParameterSource()
            .addValue("id", rent.id)
            .addValue("status", rent.status)

        jdbcTemplate.update(
            """
                UPDATE rent 
                SET rent_status = :status
                WHERE rent.id = :id
            """,
            parameters
        )
    }

    override fun findByRenterId(id: Long): List<RentEntity> {
        val parameters = MapSqlParameterSource()
            .addValue("renterId", id)
        return jdbcTemplate.query(
            """
                $SQL_SELECT_ENTITY
                WHERE
                   r.user_id = :renterId AND r.rent_status != 'DELETED'
                GROUP BY r.id, p.id, u.id, up.id
            """, parameters, rowMapper
        )
    }

    override fun findByRenterId(
        pageNumber: Long,
        limit: Int,
        userId: Long
    ): List<RentEntity> {
        val parameters = MapSqlParameterSource()
            .addValue("renterId", userId)
            .addValue("limit", limit)
            .addValue("offset", (pageNumber - 1) * limit)

        return jdbcTemplate.query(
            """
                $SQL_SELECT_ENTITY
                WHERE
                   r.user_id = :renterId AND r.rent_status != 'DELETED'
                GROUP BY r.id, p.id, u.id, up.id
                LIMIT :limit OFFSET :offset
            """, parameters, rowMapper
        )
    }

    override fun create(
        placeId: Long,
        renterId: Long
    ): Long {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        val parameters = MapSqlParameterSource()
        parameters.addValue("placeId", placeId)
        parameters.addValue("userId", renterId)
        parameters.addValue("status", RentConfirmationStatus.OPEN.name)

        jdbcTemplate.update(
            """
                INSERT INTO rent (place_id, user_id, rent_status) 
                VALUES (:placeId, :userId, :status)
            """,
            parameters, keyHolder, arrayOf("id")
        )

        return keyHolder.key?.toLong() ?: -1
    }

    override fun countByRenterId(userId: Long): Int {
        val parameters = MapSqlParameterSource()
            .addValue("renterId", userId)

        return jdbcTemplate.queryForObject(
            """
                SELECT 
                    COUNT(distinct r.id) as count
                FROM 
                    rent r
                WHERE
                    r.user_id = :renterId AND r.rent_status != 'DELETED'
            """, parameters
        ) { rs, _ -> rs.getInt("count") }!!
    }

    companion object {
        private const val SQL_SELECT_ENTITY =
            """
                SELECT
                    r.id,
                    r.place_id,
                    r.user_id,
                    r.rent_status,
                    r.rent_admin_id,
                    r.rent_ban_reason,
            
                    u.id,
                    u.user_email,
                    u.password,
                    u.role,
                    u.first_name,
                    u.last_name,
                    u.middle_name,
                    u.user_phone,
                    u.user_image,
                    u.inn,
                    u.organization,
                    u.logo_image,
                    u.job_title,
                    u.is_activated,
                    u.activation_code,
                    u.is_banned,
                    u.user_admin_id,
                    u.user_ban_reason,
                    u.user_type,
                    
                    p.id,
                    p.place_type,
                    p.place_name,
                    p.specialization,
                    p.place_description,
                    p.place_address,
                    p.place_phone,
                    p.place_email,
                    p.place_website,
                    p.level_number,
                    p.full_area,
                    p.rentable_area,
                    p.capacity_min,
                    p.capacity_max,
                    p.parking,
                    p.services,
                    p.rules,
                    p.accessibility,
                    p.facilities,
                    p.equipments,
                    p.place_status,
                    p.place_ban_reason,
                    p.place_admin_id,
                    p.area_id,
                    p.place_coordinates_id,
                    0 as min_price, null as time_start, null as time_end,
                    0 as rating, 0 as rate_count,
                    
                    up.id,
                    up.user_email,
                    up.password,
                    up.role,
                    up.first_name,
                    up.last_name,
                    up.middle_name,
                    up.user_phone,
                    up.user_image,
                    up.inn,
                    up.organization,
                    up.logo_image,
                    up.job_title,
                    up.is_activated,
                    up.activation_code,
                    up.is_banned,
                    up.user_admin_id,
                    up.user_ban_reason,
                    up.user_type,
                    array_agg(rs.id)
                    
                    FROM rent r
                         JOIN rent_slot__rent rs_r on rs_r.rent_id = r.id
                         JOIN rent_slot rs         on rs.id = rs_r.rent_slot_id
                         JOIN aki_user u           on r.user_id = u.id
                         JOIN place p              on r.place_id = p.id
                         JOIN aki_user up          on p.user_id = up.id
            """
    }
}
