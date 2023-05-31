package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.RentEntity
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class RentRowMapper(
    private val placeRowMapper: PlaceRowMapper,
    private val akiUserRowMapper: AkiUserRowMapper,
) : RowMapper<RentEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): RentEntity {
        val arrayIds = rs.getString("array_agg").replace("\"", "")

        return RentEntity(
            id = rs.getLong("id"),
            place = placeRowMapper.mapRow(rs, rowNum),
            user = akiUserRowMapper.mapRow(rs, rowNum),
            rentSlotIds = arrayIds.substring(1, arrayIds.length-1).split(',').map { it.toLong() },
            status = rs.getString("rent_status"),
            agreement = rs.getString("agreement"),
            banReason = rs.getString("rent_ban_reason"),
            admin = rs.getLong("rent_admin_id")
        )
    }
}
