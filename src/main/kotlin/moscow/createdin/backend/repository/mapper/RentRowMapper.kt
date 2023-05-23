package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.RentEntity
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class RentRowMapper(
    private val placeRowMapper: PlaceRowMapper,
    private val akiUserRowMapper: AkiUserRowMapper,
    private val akiUserAdminRowMapper: AkiUserAdminRowMapper
) : RowMapper<RentEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): RentEntity = RentEntity(
        id = rs.getLong("id"),
        place = if (rs.getLongOrNull("place_id") == null) {
            null
        } else {
            placeRowMapper.mapRow(rs, rowNum)
        },
        user = if (rs.getLongOrNull("user_id") == null) {
            null
        } else {
            akiUserRowMapper.mapRow(rs, rowNum)
        },
        status = rs.getString("status"),
        banReason = rs.getString("ban_reason"),
        admin = if (rs.getLongOrNull("admin_id") == null) {
            null
        } else {
            akiUserAdminRowMapper.mapRow(rs, rowNum)
        },
    )
}
