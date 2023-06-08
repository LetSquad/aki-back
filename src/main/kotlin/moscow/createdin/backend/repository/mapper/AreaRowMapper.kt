package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.AreaEntity
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class AreaRowMapper(
    private val coordinatesRowMapper: CoordinatesRowMapper,
    private val userRowMapper: AkiUserRowMapper
) : RowMapper<AreaEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): AreaEntity = AreaEntity(
        id = rs.getLong("id"),
        user = userRowMapper.mapRow(rs, rowNum),
        name = rs.getString("area_name"),
        description = rs.getString("area_description"),
        areaImage = rs.getString("area_image"),
        address = rs.getString("area_address"),
        website = rs.getString("area_website"),
        email = rs.getString("area_email"),
        phone = rs.getString("area_phone"),
        coordinates = if (rs.getLongOrNull("coordinates_id") == null) {
            null
        } else {
            coordinatesRowMapper.mapRow(rs, rowNum)
        },
        status = rs.getString("area_status"),
        banReason = rs.getString("area_ban_reason"),
        admin = rs.getLong("area_admin_id")
    )
}
