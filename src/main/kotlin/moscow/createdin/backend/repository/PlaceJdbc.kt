package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.PlaceEntity
import moscow.createdin.backend.model.enums.PlaceSortDirection
import moscow.createdin.backend.model.enums.PlaceSortType
import moscow.createdin.backend.model.enums.SpecializationType
import moscow.createdin.backend.repository.mapper.PlaceRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.Array
import java.sql.Timestamp


@Repository
class PlaceJdbc(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val rowMapper: PlaceRowMapper
) : PlaceRepository {

    override fun countByFilter(
        specialization: List<SpecializationType>,
        rating: Int?,
        priceMin: Int?,
        priceMax: Int?,
        capacityMin: Int?,
        capacityMax: Int?,
        squareMin: Int?,
        squareMax: Int?,
        levelNumberMin: Int?,
        levelNumberMax: Int?,
        withParking: Boolean?,
        dateFrom: Timestamp?,
        dateTo: Timestamp?
    ): Int {
        val namedParameters =
            getNamedParameters(
                specialization, rating, priceMin, priceMax, capacityMin, capacityMax, squareMin,
                squareMax, levelNumberMin, levelNumberMax, withParking, dateFrom, dateTo
            )
        return jdbcTemplate.queryForObject(
            """
                SELECT COUNT(distinct p.id) as count
                FROM place p
                INNER JOIN rent_slot rs on p.id = rs.place_id
                LEFT JOIN (
    SELECT
        min(rs.price) as min_price,
        max(rs.price) as max_price,
        count(distinct r.id) as popular,
        avg(pr.rating) as rating,
        min(rs.time_start) as time_start,
        min(rs.time_end) as time_end,
        rs.place_id as place_id
    FROM rent_slot rs
             LEFT JOIN rent r on rs.place_id = r.place_id
             LEFT JOIN place_review pr on r.id = pr.rent_id
    WHERE rs.rent_slot_status = 'OPEN'
    GROUP BY rs.place_id
) as st on p.id = st.place_id
                WHERE (:withSpecializationFilter = false OR :specialization && specialization)
                AND (:withRatingFilter = false OR :rating >= (rating))
                AND (:withPriceMinFilter = false OR :priceMin <= min_price)
                AND (:withPriceMaxFilter = false OR :priceMax >= min_price)
                AND (:withCapacityMinFilter = false OR :capacityMin <= capacity_min)
                AND (:withCapacityMaxFilter = false OR :capacityMax >= capacity_max)
                AND (:withSquareMinFilter = false OR :squareMin <= full_area)
                AND (:withSquareMaxFilter = false OR :squareMax >= full_area)
                AND (:withLevelMinFilter = false OR :levelNumberMin <= level_number)
                AND (:withLevelMaxFilter = false OR :levelNumberMax >= level_number)
                AND (:withParkingFilter = false OR p.parking = :withParking)
                AND (:withDateFromFilter = false OR rs.time_start >= :dateFrom)
                AND (:withDateToFilter = false OR rs.time_end <= :dateTo)
                AND p.place_status = 'VERIFIED' 
                AND rs.rent_slot_status = 'OPEN' 
            """, namedParameters
        ) { rs, _ -> rs.getInt("count") }!!
    }

    override fun findAll(
        specialization: List<SpecializationType>,
        rating: Int?,
        priceMin: Int?,
        priceMax: Int?,
        capacityMin: Int?,
        capacityMax: Int?,
        squareMin: Int?,
        squareMax: Int?,
        levelNumberMin: Int?,
        levelNumberMax: Int?,
        withParking: Boolean?,
        dateFrom: Timestamp?,
        dateTo: Timestamp?,

        pageNumber: Long,
        limit: Int,
        sortType: PlaceSortType,
        sortDirection: PlaceSortDirection
    ): List<PlaceEntity> {
        val sortTypeColumnName = sortType.columnName
        val query = """
            $SQL_SELECT_FILTER_ENTITY
                WHERE (:withSpecializationFilter = false OR :specialization && specialization)
                AND (:withRatingFilter = false OR :rating >= (rating))
                AND (:withPriceMinFilter = false OR :priceMin <= min_price)
                AND (:withPriceMaxFilter = false OR :priceMax >= min_price)
                AND (:withCapacityMinFilter = false OR :capacityMin <= capacity_min)
                AND (:withCapacityMaxFilter = false OR :capacityMax >= capacity_max)
                AND (:withSquareMinFilter = false OR :squareMin <= full_area)
                AND (:withSquareMaxFilter = false OR :squareMax >= full_area)
                AND (:withLevelMinFilter = false OR :levelNumberMin <= level_number)
                AND (:withLevelMaxFilter = false OR :levelNumberMax >= level_number)
                AND (:withParkingFilter = false OR p.parking = :withParking)
                AND (:withDateFromFilter = false OR rs.time_start >= :dateFrom)
                AND (:withDateToFilter = false OR rs.time_end <= :dateTo)
                AND p.place_status = 'VERIFIED' 
                AND rs.rent_slot_status = 'OPEN'
                ORDER BY $sortTypeColumnName $sortDirection
                LIMIT :limit OFFSET :offset
        """
        val namedParameters =
            getNamedParameters(
                specialization, rating, priceMin, priceMax, capacityMin, capacityMax,
                squareMin, squareMax, levelNumberMin, levelNumberMax, withParking,
                dateFrom, dateTo
            )
        namedParameters.addValue("limit", limit)
        namedParameters.addValue("offset", (pageNumber - 1) * limit)
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
        parameters.addValue("specialization", createSqlArray(place.specialization))
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
        parameters.addValue("capacity_min", place.minCapacity)
        parameters.addValue("capacity_max", place.maxCapacity)
        parameters.addValue("parking", place.parking)
        parameters.addValue("ban_reason", place.banReason)
        parameters.addValue("admin_id", place.admin)
        parameters.addValue("status", place.status)

        jdbcTemplate.update(
            """
                INSERT INTO place (user_id, area_id, place_type, place_name, specialization, place_description, place_address, place_coordinates_id, place_phone, place_email, 
                place_website, level_number, services, rules, accessibility, full_area, rentable_area, facilities, equipments, capacity_min, 
                capacity_max, place_status, place_ban_reason, place_admin_id, parking) 
                VALUES (:user_id, :area_id, :type, :name, :specialization, :description, :address, :coordinates_id, :phone, :email, 
                :website, :level_number, :services, :rules, :accessibility, :full_area, :rentable_area, :facilities, :equipments, :capacity_min, 
                :capacity_max, :status, :ban_reason, :admin_id, :parking)
            """,
            parameters, keyHolder, arrayOf("id")
        )

        return keyHolder.key?.toLong() ?: -1
    }

    private fun createSqlArray(list: List<SpecializationType>): Array {
        return jdbcTemplate.jdbcTemplate.dataSource!!.connection.use {
            it.createArrayOf(
                "SPECIALIZATION_ENUM",
                list.toTypedArray()
            )
        }
    }

    override fun update(place: PlaceEntity) {
        val parameters = MapSqlParameterSource()
        parameters.addValue("name", place.name)
        parameters.addValue("email", place.email)
        parameters.addValue("website", place.website)
        parameters.addValue("specialization", createSqlArray(place.specialization))
        parameters.addValue("phone", place.phone)
        parameters.addValue("description", place.description)
        parameters.addValue("rentableArea", place.rentableArea)
        parameters.addValue("minCapacity", place.minCapacity)
        parameters.addValue("maxCapacity", place.maxCapacity)
        parameters.addValue("services", place.services)
        parameters.addValue("equipments", place.equipments)
        parameters.addValue("facilities", place.facilities)
        parameters.addValue("parking", place.parking)
        parameters.addValue("id", place.id)
        parameters.addValue("adminId", place.admin)
        parameters.addValue("status", place.status)
        parameters.addValue("banReason", place.banReason)

        var sqlSet = """
                    place_name = :name, 
                    place_email = :email, 
                    place_website = :website, 
                    specialization = :specialization, 
                    place_phone = :phone, 
                    place_description = :description, 
                    rentable_area = :rentableArea, 
                    capacity_min = :minCapacity,
                    capacity_max = :maxCapacity, 
                    services = :services, 
                    equipments = :equipments, 
                    facilities = :facilities,
                    parking = :parking,
                    
                    place_status = :status,
                    place_ban_reason = :banReason
        """
        if (place.admin != 0L) {
            sqlSet += ",place_admin_id = :adminId"
        }

        jdbcTemplate.update(
            """
                UPDATE 
                    place 
                SET 
                    $sqlSet
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

    override fun countByUserId(userId: Long): Int {
        val parameters = MapSqlParameterSource()
            .addValue("userId", userId)

        return jdbcTemplate.queryForObject(
            """
                SELECT COUNT(distinct p.id) as count
                FROM place p
                WHERE p.user_id = :userId
            """, parameters
        ) { rs, _ -> rs.getInt("count") }!!
    }

    override fun findUnverified(
        pageNumber: Long,
        limit: Int
    ): List<PlaceEntity> {
        val parameters = MapSqlParameterSource()
            .addValue("limit", limit)
            .addValue("offset", (pageNumber - 1) * limit)

        return jdbcTemplate.query(
            """
                $SQL_SELECT_ENTITY
                WHERE p.place_status = 'UNVERIFIED' 
                LIMIT :limit OFFSET :offset
            """, parameters, rowMapper
        )
    }

    override fun countUnverified(): Int {
        val parameters = MapSqlParameterSource()

        return jdbcTemplate.queryForObject(
            """
                SELECT COUNT(distinct p.id) as count
                FROM place p
                WHERE p.place_status = 'UNVERIFIED'
            """, parameters
        ) { rs, _ -> rs.getInt("count") }!!
    }

    override fun findByUserId(
        pageNumber: Long,
        limit: Int,
        userId: Long
    ): List<PlaceEntity> {
        val parameters = MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("limit", limit)
            .addValue("offset", (pageNumber - 1) * limit)

        return jdbcTemplate.query(
            """
                $SQL_SELECT_ENTITY
                WHERE p.user_id = :userId AND p.place_status != 'DELETED' 
                LIMIT :limit OFFSET :offset
            """, parameters, rowMapper
        )
    }

    private fun getNamedParameters(
        specialization: List<SpecializationType>,
        rating: Int?,
        priceMin: Int?,
        priceMax: Int?,
        capacityMin: Int?,
        capacityMax: Int?,
        squareMin: Int?,
        squareMax: Int?,
        levelNumberMin: Int?,
        levelNumberMax: Int?,
        withParking: Boolean?,
        dateFrom: Timestamp?,
        dateTo: Timestamp?
    ): MapSqlParameterSource {
        val mapSqlParameterSource = MapSqlParameterSource()
        return mapSqlParameterSource
            .addValue("withSpecializationFilter", specialization.isNotEmpty())
            .addValue("specialization", createSqlArray(specialization))
            .addValue("rating", rating)
            .addValue("withRatingFilter", rating != null)
            .addValue("withPriceMinFilter", priceMin != null)
            .addValue("withPriceMaxFilter", priceMax != null)
            .addValue("priceMin", priceMin)
            .addValue("priceMax", priceMax)
            .addValue("withCapacityMinFilter", capacityMin != null)
            .addValue("withCapacityMaxFilter", capacityMax != null)
            .addValue("capacityMin", capacityMin)
            .addValue("capacityMax", capacityMax)
            .addValue("withSquareMinFilter", squareMin != null)
            .addValue("withSquareMaxFilter", squareMax != null)
            .addValue("squareMin", squareMin)
            .addValue("squareMax", squareMax)
            .addValue("withLevelMinFilter", levelNumberMin != null)
            .addValue("withLevelMaxFilter", levelNumberMax != null)
            .addValue("levelNumberMin", levelNumberMin)
            .addValue("levelNumberMax", levelNumberMax)
            .addValue("withParkingFilter", withParking != false)
            .addValue("withParking", withParking)
            .addValue("withDateFromFilter", dateFrom != null)
            .addValue("withDateToFilter", dateTo != null)
            .addValue("dateFrom", dateFrom)
            .addValue("dateTo", dateTo)
    }

    companion object {
        private const val SQL_SELECT_FILTER_ENTITY =
            """
                SELECT distinct
                    p.id,
                    p.user_id,
                    p.area_id,
                    p.place_type,
                    p.place_coordinates_id,
                    p.place_description,
                    p.place_name,
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
                    p.capacity_min,
                    p.capacity_max,
                    p.parking,
                    p.place_status,
                    p.place_admin_id,
                    p.place_ban_reason,
                
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
                
                    st.popular,
                    st.rate_count,
                    st.rating,
                    st.min_price,
                    st.max_price,
                    st.time_start,
                    st.time_end
                    /**/
                FROM place p
                INNER JOIN aki_user u on p.user_id = u.id
                LEFT JOIN area a on p.area_id = a.id
                INNER JOIN rent_slot rs on p.id = rs.place_id
                LEFT JOIN (
    SELECT
        min(rs.price) as min_price,
        max(rs.price) as max_price,
        count(distinct r.id) as popular,
        count(distinct pr.id) as rate_count,
        avg(pr.rating) as rating,
        min(rs.time_start) as time_start,
        min(rs.time_end) as time_end,
        rs.place_id as place_id
    FROM rent_slot rs
             LEFT JOIN rent r on rs.place_id = r.place_id
             LEFT JOIN place_review pr on r.id = pr.rent_id
    WHERE rs.rent_slot_status = 'OPEN'
    GROUP BY rs.place_id
) as st on p.id = st.place_id
                    """

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
                    p.parking, 
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
                    
                    0 as min_price, 
                    null as time_start, 
                    null as time_end,
                    st.rate_count,
                    st.rating
                FROM place p
                
                INNER JOIN aki_user u on p.user_id = u.id
                LEFT JOIN area a on p.area_id = a.id 
                LEFT JOIN (
    SELECT
        min(rs.price) as min_price,
        max(rs.price) as max_price,
        count(distinct r.id) as popular,
        count(distinct pr.id) as rate_count,
        avg(pr.rating) as rating,
        min(rs.time_start) as time_start,
        min(rs.time_end) as time_end,
        rs.place_id as place_id
    FROM rent_slot rs
             LEFT JOIN rent r on rs.place_id = r.place_id
             LEFT JOIN place_review pr on r.id = pr.rent_id
    WHERE rs.rent_slot_status = 'OPEN'
    GROUP BY rs.place_id
) as st on p.id = st.place_id
            """
    }
}
