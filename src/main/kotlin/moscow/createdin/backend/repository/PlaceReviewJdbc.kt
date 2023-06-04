package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.PlaceReviewEntity
import moscow.createdin.backend.repository.mapper.PlaceReviewRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository


@Repository
class PlaceReviewJdbc(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val rowMapper: PlaceReviewRowMapper
) : PlaceReviewRepository {
    override fun save(placeReviewEntity: PlaceReviewEntity): Long {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        val parameters = MapSqlParameterSource()
        parameters.addValue("rentId", placeReviewEntity.rent)
        parameters.addValue("rating", placeReviewEntity.rating)
        parameters.addValue("reviewText", placeReviewEntity.reviewText)
        parameters.addValue("status", placeReviewEntity.status)
        parameters.addValue("banReason", placeReviewEntity.banReason)
        parameters.addValue("adminId", placeReviewEntity.admin)

        jdbcTemplate.update(
            """
                INSERT INTO place_review (rent_id, rating, review_text, place_review_status, place_review_ban_reason, place_review_admin_id) 
                VALUES (:rentId, :rating, :reviewText, :status, :banReason, :adminId)
            """,
            parameters, keyHolder, arrayOf("id")
        )

        return keyHolder.key?.toLong() ?: -1
    }

    override fun findByPlaceId(placeId: Long): List<PlaceReviewEntity> {
        val parameters = MapSqlParameterSource()
            .addValue("placeId", placeId)

        return jdbcTemplate.query(
            """
                SELECT pr.id, rent_id, rating, review_text, place_review_status, place_review_ban_reason, 
                 place_review_admin_id FROM place_review pr 
                INNER JOIN rent r on pr.rent_id = r.id
                WHERE place_id = :placeId
            """, parameters, rowMapper
        )
    }
}
