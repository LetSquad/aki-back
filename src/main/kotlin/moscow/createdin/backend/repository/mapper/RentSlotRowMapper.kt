package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.RentSlotEntity
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class RentSlotRowMapper : RowMapper<RentSlotEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): RentSlotEntity = RentSlotEntity(
        id = rs.getLong("id"),
        placeId = rs.getLong("place_id"),
        timeStart = rs.getTimestamp("time_start"),
        timeEnd = rs.getTimestamp("time_end"),
        status = rs.getString("rent_slot_status"),
        price = rs.getBigDecimal("price"),
    )
}
