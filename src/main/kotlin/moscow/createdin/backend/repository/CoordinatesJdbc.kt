package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.CoordinatesEntity
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository

@Repository
class CoordinatesJdbc(private val jdbcTemplate: NamedParameterJdbcTemplate) : CoordinatesRepository {

    override fun save(coordinates: CoordinatesEntity): Long {
        val parameters = MapSqlParameterSource()
        parameters.addValue("id", coordinates.id)
        parameters.addValue("latitude", coordinates.latitude)
        parameters.addValue("longitude", coordinates.longitude)

        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            """
                INSERT INTO coordinates(latitude, longitude) 
                VALUES (:latitude, :longitude) 
            """, parameters, keyHolder, arrayOf("id")
        )

        return keyHolder.key?.toLong() ?: -1
    }

    override fun delete(id: Long) {
        val parameters = MapSqlParameterSource()
        parameters.addValue("id", id)

        jdbcTemplate.update(
            """DELETE FROM coordinates WHERE id = :id""",
            parameters
        )
    }
}
