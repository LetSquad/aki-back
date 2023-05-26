package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.PlaceEntity
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class PlaceRowMapper(
    private val coordinatesRowMapper: CoordinatesRowMapper,
    private val areaRowMapper: AreaRowMapper,
    private val akiUserRowMapper: AkiUserRowMapper,
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
        specialization = rs.getString("specialization"),
        description = rs.getString("place_description"),
        address = rs.getString("place_address"),
        phone = rs.getString("place_phone"),
        email = rs.getString("place_email"),
        website = rs.getString("place_website"),
        levelNumber = rs.getInt("level_number"),
        fullArea = rs.getInt("full_area"),
        rentableArea = rs.getInt("rentable_area"),
        capacityMin = rs.getInt("capacity_min"),
        capacityMax = rs.getInt("capacity_max"),
        services = rs.getString("services"),
        rules = rs.getString("rules"),
        accessibility = rs.getString("accessibility"),
        facilities = rs.getString("facilities"),
        equipments = rs.getString("equipments"),
        status = rs.getString("place_status"),
        banReason = rs.getString("place_ban_reason"),
        admin = rs.getLong("place_admin_id")
    )
}
