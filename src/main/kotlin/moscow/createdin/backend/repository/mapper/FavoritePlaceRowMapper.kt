package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.FavoritePlaceEntity
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class FavoritePlaceRowMapper(
) : RowMapper<FavoritePlaceEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): FavoritePlaceEntity {

        return FavoritePlaceEntity(
            id = rs.getLong("id"),
            placeId = rs.getLong("place_id"),
            userId = rs.getLong("user_id")
        )
    }
}
