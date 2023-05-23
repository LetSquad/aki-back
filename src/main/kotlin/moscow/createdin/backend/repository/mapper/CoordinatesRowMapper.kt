package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.CoordinatesEntity
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class CoordinatesRowMapper : RowMapper<CoordinatesEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): CoordinatesEntity = CoordinatesEntity(
        id = rs.getLong("id"),
        longitude = rs.getBigDecimal("longitude"),
        latitude = rs.getBigDecimal("latitude")
    )
}
