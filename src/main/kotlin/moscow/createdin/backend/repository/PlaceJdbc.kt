package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.PlaceEntity
import moscow.createdin.backend.model.enums.PlaceSortDirection
import moscow.createdin.backend.repository.mapper.PlaceRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository


@Repository
class PlaceJdbc(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val rowMapper: PlaceRowMapper
) : PlaceRepository {
    override fun countByFilter(
        specialization: String?, capacity: Int?, fullAreaMin: Int?, fullAreaMax: Int?, levelNumberMin: Int?,
        levelNumberMax: Int?, parking: Boolean?
    ): Int {
        val namedParameters =
            getNamedParameters(
                specialization, capacity, fullAreaMin, fullAreaMax,
                levelNumberMin, levelNumberMax, parking
            )
        return jdbcTemplate.queryForObject(
            """
                SELECT COUNT(distinct p.id) as count
                FROM place p
                INNER JOIN rent_slot rs on p.id = rs.place_id
                WHERE (:specialization = '' OR specialization like :specialization)
                AND (:withCapacityFilter = false OR :capacity >= capacity_min AND :capacity <= capacity_max)
                AND (:withAreaFilter = false OR :fullAreaMin <= full_area AND :fullAreaMax >= full_area)
                AND (:withLevelFilter = false OR :levelNumberMin <= level_number AND :levelNumberMax >= level_number)
                AND p.status = 'CONFIRMED' AND rs.status = 'OPEN' 
            """, namedParameters
        ) { rs, _ -> rs.getInt("count") }!!
    }

    override fun findAll(
        specialization: String?, capacity: Int?, fullAreaMin: Int?, fullAreaMax: Int?, levelNumberMin: Int?,
        levelNumberMax: Int?, parking: Boolean?, pageNumber: Long, limit: Int, sortType: String,
        sortDirection: PlaceSortDirection
    ): List<PlaceEntity> {
        val query = """
            $SQL_SELECT_FILTER_ENTITY
                WHERE (:specialization = '' OR specialization like :specialization)
                AND (:withCapacityFilter = false OR :capacity >= capacity_min AND :capacity <= capacity_max)
                AND (:withAreaFilter = false OR :fullAreaMin <= full_area AND :fullAreaMax >= full_area)
                AND (:withLevelFilter = false OR :levelNumberMin <= level_number AND :levelNumberMax >= level_number)
                AND p.status = 'CONFIRMED'
                ORDER BY $sortType $sortDirection
                LIMIT :limit OFFSET :offset
        """
        val namedParameters =
            getNamedParameters(
                specialization, capacity, fullAreaMin, fullAreaMax,
                levelNumberMin, levelNumberMax, parking
            )
        namedParameters.addValue("limit", limit)
        namedParameters.addValue("offset", (pageNumber - 1) * limit)
        namedParameters.addValue("sort", sortType)
        namedParameters.addValue("sortDirection", sortDirection.name)
        return jdbcTemplate.query(
            query, namedParameters, rowMapper
        )
    }

    override fun save(place: PlaceEntity): Long {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        val parameters = MapSqlParameterSource()
        parameters.addValue("user_id", place.user.id)
        parameters.addValue("area_id", place.area?.id)
        parameters.addValue("type", place.type)
        parameters.addValue("name", place.name)
        parameters.addValue("specialization", place.specialization)
        parameters.addValue("description", place.description)
        parameters.addValue("address", place.address)
        parameters.addValue("coordinates_id", place.coordinates?.id)
        parameters.addValue("phone", place.phone)
        parameters.addValue("email", place.email)
        parameters.addValue("website", place.website)
        parameters.addValue("level_number", place.levelNumber)
        parameters.addValue("services", place.services)
        parameters.addValue("rules", place.rules)
        parameters.addValue("accessibility", place.accessibility)
        parameters.addValue("full_area", place.fullArea)
        parameters.addValue("rentable_area", place.rentableArea)
        parameters.addValue("facilities", place.facilities)
        parameters.addValue("equipments", place.equipments)
        parameters.addValue("capacity_min", place.capacityMin)
        parameters.addValue("capacity_max", place.capacityMax)
        parameters.addValue("ban_reason", place.banReason)
        parameters.addValue("admin_id", place.admin)
        parameters.addValue("status", place.status)

        jdbcTemplate.update(
            """
                INSERT INTO place (user_id, area_id, place_type, place_name, specialization, place_description, place_address, place_coordinates_id, place_phone, place_email, 
                place_website, level_number, services, rules, accessibility, full_area, rentable_area, facilities, equipments, capacity_min, 
                capacity_max, place_status, place_ban_reason, place_admin_id) 
                VALUES (:user_id, :area_id, :type, :name, :specialization, :description, :address, :coordinates_id, :phone, :email, 
                :website, :level_number, :services, :rules, :accessibility, :full_area, :rentable_area, :facilities, :equipments, :capacity_min, 
                :capacity_max, :status, :ban_reason, :admin_id)
            """,
            parameters, keyHolder, arrayOf("id")
        )

        return keyHolder.key?.toLong() ?: -1
    }

    override fun update(place: PlaceEntity) {
        val parameters = MapSqlParameterSource()
        parameters.addValue("name", place.name)
        parameters.addValue("email", place.email)
        parameters.addValue("website", place.website)
        parameters.addValue("specialization", place.specialization)
        parameters.addValue("phone", place.phone)
        parameters.addValue("description", place.description)
        parameters.addValue("rentableArea", place.rentableArea)
        parameters.addValue("capacityMin", place.capacityMin)
        parameters.addValue("capacityMax", place.capacityMax)
        parameters.addValue("services", place.services)
        parameters.addValue("equipments", place.equipments)
        parameters.addValue("facilities", place.facilities)
        parameters.addValue("id", place.id)

        jdbcTemplate.update(
            """
                UPDATE place SET name = :name, email = :email, website = :website, specialization = :specialization, 
                    phone = :phone, description = :description, rentable_area = :rentableArea, capacity_min = :capacityMin,
                    capacity_max = :capacityMax, services = :services, equipments = :equipments, facilities = :facilities 
                WHERE place.id = :id
            """,
            parameters
        )
    }

    override fun findById(id: Long): PlaceEntity {
        val query = """
            $SQL_SELECT_ENTITY
                WHERE p.id = :id
        """
        val namedParameters = MapSqlParameterSource()
            .addValue("id", id)
        return jdbcTemplate.queryForObject(
            query, namedParameters, rowMapper
        )!!
    }

    override fun findByUserId(userId: Long): List<PlaceEntity> {
        val parameters = MapSqlParameterSource()
            .addValue("userId", userId)
        return jdbcTemplate.query(
            """
                $SQL_SELECT_ENTITY
                WHERE p.user_id = :userId
            """, parameters, rowMapper
        )
    }

    private fun getNamedParameters(
        specialization: String?, capacity: Int?, fullAreaMin: Int?, fullAreaMax: Int?, levelNumberMin: Int?,
        levelNumberMax: Int?, parking: Boolean?
    ): MapSqlParameterSource {
        val mapSqlParameterSource = MapSqlParameterSource()
        return mapSqlParameterSource
            .addValue("specialization", specialization.orEmpty())
            .addValue("withCapacityFilter", capacity != null)
            .addValue("capacity", capacity)
            .addValue("withAreaFilter", fullAreaMin != null && fullAreaMax != null)
            .addValue("fullAreaMin", fullAreaMin)
            .addValue("fullAreaMax", fullAreaMax)
            .addValue("withLevelFilter", levelNumberMin != null && levelNumberMax != null)
            .addValue("levelNumberMin", levelNumberMin)
            .addValue("levelNumberMax", levelNumberMax)
            .addValue("parking", parking)
    }

    companion object {
        private const val SQL_SELECT_FILTER_ENTITY =
            "SELECT distinct p.id, p.user_id as user_id, p.area_id, p.place_type, p.place_coordinates_id, p.place_description, p.place_name, p.specialization, p.place_address, p.place_phone, p.place_email, " +
                    "p.place_website, p.level_number, p.services, p.rules, p.accessibility, p.full_area, p.rentable_area, p.facilities, " +
                    "p.equipments, p.capacity_min, p.capacity_max, p.place_status, p.place_admin_id, p.place_ban_reason, " +

                    "u.id, u.user_email, u.password, u.role, u.user_type, " +
                    "u.first_name, u.last_name, u.middle_name, " +
                    "u.user_phone, u.user_image, u.inn, u.organization, u.logo_image, u.job_title, u.is_activated, u.is_banned, u.user_ban_reason, u.user_admin_id, " +

                    "a.id, a.user_id, a.area_name, a.area_description, a.area_image, a.area_status, a.area_address, a.area_website, a.area_email, a.area_phone, a.area_coordinates_id, " +
                    "a.area_ban_reason, a.area_admin_id, u.activation_code," +

                    "st.popular_count, " +
                    "st.avg_rating, " +
                    "st.min_price," +
                    "st.time_start ," +
                    "st.time_end  " +
                    "FROM place p " +
                    "INNER JOIN aki_user u on p.user_id = u.id " +
                    "LEFT JOIN area a on p.area_id = a.id " +
                    "INNER JOIN (SELECT count(distinct r.id) as popular_count, avg(rating) as avg_rating, min(price) as min_price, " +
                    "min(time_start) as time_start, min(time_end) as time_end, r.place_id " +
                    "                     from rent r " +
                    "                              INNER JOIN rent_slot rs on rs.place_id = r.place_id " +
                    "                              INNER JOIN place_review pr on r.id = pr.rent_id " +
                    "                     where rs.status = 'OPEN'" +
                    "                     group by r.place_id) as st on p.id = st.place_id "

        private const val SQL_SELECT_ENTITY =
            """
                SELECT distinct 
                    p.id, 
                    p.user_id,
                    p.area_id, 
                    p.place_type, 
                    p.place_name, 
                    p.place_description, 
                    p.specialization, 
                    p.place_address, 
                    p.place_phone, 
                    p.place_email, 
                    p.place_website, 
                    p.level_number, 
                    p.services, 
                    p.rules, 
                    p.accessibility, 
                    p.full_area, 
                    p.rentable_area, 
                    p.facilities, 
                    p.equipments, 
                    p.place_coordinates_id, 
                    p.capacity_min, 
                    p.capacity_max, 
                    p.place_ban_reason, 
                    p.place_status, 
                    p.place_admin_id, 
                    
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
                    u.logo_image, 
                    u.job_title, 
                    u.is_activated, 
                    u.is_banned, 
                    u.user_ban_reason, 
                    u.user_admin_id,
                    u.activation_code,
                     
                    a.id, 
                    a.user_id, 
                    a.area_name, 
                    a.area_description, 
                    a.area_image, 
                    a.area_status, 
                    a.area_address, 
                    a.area_website, 
                    a.area_email, 
                    a.area_phone, 
                    a.area_coordinates_id, 
                    a.area_ban_reason, 
                    a.area_admin_id,
                    
                    0 as min_price, null as time_start, null as time_end 
                FROM place p
                
                INNER JOIN aki_user u on p.user_id = u.id
                LEFT JOIN area a on p.area_id = a.id 
            """
    }
}
