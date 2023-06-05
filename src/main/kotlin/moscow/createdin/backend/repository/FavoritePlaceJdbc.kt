package moscow.createdin.backend.repository

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository


@Repository
class FavoritePlaceJdbc(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : FavoritePlaceRepository {
    override fun save(placeId: Long, userId: Long?): Long {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        val parameters = MapSqlParameterSource()
        parameters.addValue("placeId", placeId)
        parameters.addValue("userId", userId)

        jdbcTemplate.update(
            """
                INSERT INTO favorite_place (place_id, user_id) 
                VALUES (:placeId, :userId)
            """,
            parameters, keyHolder, arrayOf("id")
        )

        return keyHolder.key?.toLong() ?: -1
    }

    override fun delete(placeId: Long, userId: Long?) {
        val parameters = MapSqlParameterSource()
        parameters.addValue("placeId", placeId)
        parameters.addValue("userId", userId)

        jdbcTemplate.update(
            """
                DELETE FROM favorite_place WHERE place_id = :placeId AND user_id = :userId
            """,
            parameters
        )
    }


}
