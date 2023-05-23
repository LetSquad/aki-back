package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.PlaceImageEntity
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class PlaceImageRowMapper : RowMapper<PlaceImageEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): PlaceImageEntity = PlaceImageEntity(
        id = rs.getLong("id"),
        placeId = rs.getLong("place_id"),
        image = rs.getString("image"),
        priority = rs.getInt("priority")
    )
}
