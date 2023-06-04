package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.PlaceReviewEntity
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class PlaceReviewRowMapper(
    private val rentRowMapper: RentRowMapper,
) : RowMapper<PlaceReviewEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): PlaceReviewEntity = PlaceReviewEntity(
        id = rs.getLong("id"),
        rent = rs.getLong("rent_id"),
        rating = rs.getInt("rating"),
        reviewText = rs.getString("review_text"),
        status = rs.getString("place_review_status"),
        banReason = rs.getString("place_review_ban_reason"),
        admin = rs.getLong("place_review_admin_id")
    )
}
