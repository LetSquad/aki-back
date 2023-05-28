package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.PlaceImageEntity
import moscow.createdin.backend.repository.mapper.PlaceImageRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository


@Repository
class PlaceImageJdbc(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val rowMapper: PlaceImageRowMapper
) : PlaceImageRepository {

    override fun findByPlaceId(placeId: Long?): List<PlaceImageEntity> {
        val query = """
            $SQL_SELECT_ENTITY
                WHERE pi.place_id = :placeId
        """
        val namedParameters = MapSqlParameterSource().addValue("placeId", placeId)
        return jdbcTemplate.query(
            query, namedParameters, rowMapper
        )
    }

    override fun save(placeImage: PlaceImageEntity) {
        val namedParameters = MapSqlParameterSource()
            .addValue("placeId", placeImage.placeId)
            .addValue("image", placeImage.image)
            .addValue("priority", placeImage.priority)
        jdbcTemplate.update(
            "INSERT INTO place_image (place_id, image, priority) VALUES (:placeId, :image, :priority)",
            namedParameters
        )
    }

    override fun deleteByPlaceId(placeId: Long) {
        val namedParameters = MapSqlParameterSource()
            .addValue("placeId", placeId)
        jdbcTemplate.update("DELETE FROM place_image WHERE place_id = :placeId", namedParameters)
    }

    companion object {
        private const val SQL_SELECT_ENTITY =
            "SELECT pi.id, pi.place_id, pi.image, pi.priority " +
                    "FROM place_image pi "
    }
}
