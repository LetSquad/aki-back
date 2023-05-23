package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.RentSlotEntity
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class RentSlotRowMapper(
    private val placeRowMapper: PlaceRowMapper,
) : RowMapper<RentSlotEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): RentSlotEntity = RentSlotEntity(
        id = rs.getLong("id"),
        place = if (rs.getLongOrNull("place_id") == null) {
            null
        } else {
            placeRowMapper.mapRow(rs, rowNum)
        },
        timeStart = rs.getDate("time_start"),
        timeEnd = rs.getDate("time_end"),
        status = rs.getString("status"),
        price = rs.getBigDecimal("price"),
    )
}
