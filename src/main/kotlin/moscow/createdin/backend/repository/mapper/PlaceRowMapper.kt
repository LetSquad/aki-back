package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.PlaceEntity
import moscow.createdin.backend.model.enums.PriceType
import moscow.createdin.backend.model.enums.SpecializationType
import org.postgresql.util.PGobject
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

@Component
class PlaceRowMapper(
    private val coordinatesRowMapper: CoordinatesRowMapper,
    private val areaRowMapper: AreaRowMapper,
    private val akiUserRowMapper: AkiUserRowMapper
) : RowMapper<PlaceEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): PlaceEntity = PlaceEntity(
        id = rs.getLong("id"),
        user = akiUserRowMapper.mapRow(rs, rowNum),
        area = if (rs.getLongOrNull("area_id") == null) {
            null
        } else {
            areaRowMapper.mapRow(rs, rowNum)
        },
        coordinates = if (rs.getLongOrNull("place_coordinates_id") == null) {
            null
        } else {
            coordinatesRowMapper.mapRow(rs, rowNum)
        },
        type = rs.getString("place_type"),
        name = rs.getString("place_name"),
        specialization = getArrayEnum(rs.getString("specialization")),
        description = rs.getString("place_description"),
        address = rs.getString("place_address"),
        phone = rs.getString("place_phone"),
        email = rs.getString("place_email"),
        website = rs.getString("place_website"),
        levelNumber = rs.getInt("level_number"),
        fullArea = rs.getInt("full_area"),
        rentableArea = rs.getInt("rentable_area"),
        minCapacity = rs.getInt("capacity_min"),
        maxCapacity = rs.getInt("capacity_max"),
        parking = rs.getBoolean("parking"),
        services = stringToPGObject(rs.getString("services")),
        rules = rs.getString("rules"),
        accessibility = rs.getString("accessibility"),
        facilities = stringToPGObject(rs.getString("facilities")),
        equipments = stringToPGObject(rs.getString("equipments")),
        status = rs.getString("place_status"),
        banReason = rs.getString("place_ban_reason"),
        admin = rs.getLong("place_admin_id"),
        minPrice = rs.getDouble("min_price"),
        priceType = findPriceType(rs.getDate("time_start"), rs.getDate("time_end")),
        rating = rs.getDouble("rating"),
        rateCount = rs.getInt("rate_count")
    )

    private fun getArrayEnum(arrayStr: String): List<SpecializationType> {
        return arrayStr.substring(1, arrayStr.length - 1)
            .split(',')
            .map { SpecializationType.valueOf(it) }
    }

    fun findPriceType(timeStart: Date?, timeEnd: Date?): String {
        if (timeStart != null && timeEnd != null) {
            val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val from = LocalDate.parse(timeStart.toString(), dateFormatter)
            val to = LocalDate.parse(timeEnd.toString(), dateFormatter)

            val period = Period.between(from, to)
            val days = period.days
            if (days >= 1) return PriceType.DAY.name
            return PriceType.HOUR.name
        }
        return PriceType.FREE.name
    }

    fun stringToPGObject(value: String?): PGobject {
        val pGobject = PGobject()
        pGobject.type = "jsonb"
        pGobject.value = value
        return pGobject
    }
}
