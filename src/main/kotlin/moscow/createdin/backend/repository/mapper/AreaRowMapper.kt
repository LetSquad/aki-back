package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.AreaEntity
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class AreaRowMapper(
    private val coordinatesRowMapper: CoordinatesRowMapper,
    private val akiUserAdminRowMapper: AkiUserAdminRowMapper,
    private val userRowMapper: AkiUserRowMapper
) : RowMapper<AreaEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): AreaEntity = AreaEntity(
        id = rs.getLong("id"),
        user = userRowMapper.mapRow(rs, rowNum),
        name = rs.getString("name"),
        description = rs.getString("description"),
        areaImage = rs.getString("area_image"),
        address = rs.getString("address"),
        website = rs.getString("website"),
        email = rs.getString("email"),
        phone = rs.getString("phone"),
        coordinates = if (rs.getLongOrNull("coordinates_id") == null) {
            null
        } else {
            coordinatesRowMapper.mapRow(rs, rowNum)
        },
        status = rs.getString("status"),
        banReason = rs.getString("ban_reason"),
        admin = if (rs.getLongOrNull("admin_id") == null) {
            null
        } else {
            akiUserAdminRowMapper.mapRow(rs, rowNum)
        }
    )
}
