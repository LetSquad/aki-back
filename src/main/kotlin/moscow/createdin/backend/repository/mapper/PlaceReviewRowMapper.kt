package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.PlaceReviewEntity
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class PlaceReviewRowMapper(
    private val rentRowMapper: RentRowMapper,
    private val akiUserAdminRowMapper: AkiUserAdminRowMapper
) : RowMapper<PlaceReviewEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): PlaceReviewEntity = PlaceReviewEntity(
        id = rs.getLong("id"),
        rent = rentRowMapper.mapRow(rs, rowNum),
        rating = rs.getInt("rating"),
        reviewText = rs.getString("reviewText"),
        status = rs.getString("status"),
        banReason = rs.getString("ban_reason"),
        admin = if (rs.getLongOrNull("admin_id") == null) {
            null
        } else {
            akiUserAdminRowMapper.mapRow(rs, rowNum)
        },
    )
}
