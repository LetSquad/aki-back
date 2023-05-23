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
    private val akiUserAdminRowMapper: AkiUserAdminRowMapper
) : RowMapper<PlaceEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): PlaceEntity = PlaceEntity(
        id = rs.getLong("id"),
        user = akiUserRowMapper.mapRow(rs, rowNum),
        area = areaRowMapper.mapRow(rs, rowNum),
        coordinates = if (rs.getLongOrNull("coordinates_id") == null) {
            null
        } else {
            coordinatesRowMapper.mapRow(rs, rowNum)
        },
        type = rs.getString("type"),
        name = rs.getString("name"),
        specialization = rs.getString("specialization"),
        description = rs.getString("description"),
        address = rs.getString("address"),
        phone = rs.getString("phone"),
        email = rs.getString("email"),
        website = rs.getString("website"),
        levelNumber = rs.getInt("level_number"),
        fullArea = rs.getInt("fullArea"),
        rentableArea = rs.getInt("rentable_area"),
        capacityMin = rs.getInt("capacity_min"),
        capacityMax = rs.getInt("capacity_max"),
        services = rs.getString("services"),
        rules = rs.getString("rules"),
        accessibility = rs.getString("accessibility"),
        facilities = rs.getString("facilities"),
        equipments = rs.getString("equipments"),
        status = rs.getString("status"),
        banReason = rs.getString("ban_reason"),
        admin = if (rs.getLongOrNull("admin_id") == null) {
            null
        } else {
            akiUserAdminRowMapper.mapRow(rs, rowNum)
        },
    )
}
