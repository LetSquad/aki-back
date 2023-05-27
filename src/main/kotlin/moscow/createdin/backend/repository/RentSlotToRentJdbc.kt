package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.RentSlotEntity
import moscow.createdin.backend.repository.mapper.RentSlotRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository


@Repository
class RentSlotToRentJdbc(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val rowMapper: RentSlotRowMapper
) : RentSlotToRentRepository {

    override fun create(
        rentId: Long,
        rentSlotIds: List<Long>
    ) {
        val parameters = rentSlotIds.map {
            MapSqlParameterSource()
                .addValue("rentSlotId", it)
                .addValue("rentId", rentId)
        }

        jdbcTemplate.batchUpdate(
            """
                INSERT 
                    INTO rent_slot__rent (rent_slot_id, rent_id) 
                VALUES (:rentSlotId, :rentId)
                """,
            parameters.toTypedArray()
        )
    }

    override fun delete(rentId: Long) {
        val parameters = MapSqlParameterSource()
            .addValue("rentId", rentId)

        jdbcTemplate.update(
            """
                DELETE FROM rent_slot__rent WHERE rent_id = :rentId
                """,
            parameters
        )
    }

    override fun findSlotsByRentId(rentId: Long): List<RentSlotEntity> {
        val parameters = MapSqlParameterSource()
            .addValue("rentId", rentId)

        return jdbcTemplate.query(
            """
                SELECT
                    rs.id,
                    rs.place_id, 
                    rs.price, 
                    rs.time_start, 
                    rs.time_end, 
                    rs.rent_slot_status, 
                
                    p.id
                FROM
                    rent_slot__rent
                JOIN 
                    rent_slot rs on rent_slot__rent.rent_slot_id = rs.id
                JOIN 
                    place p on rs.place_id = p.id
                
                WHERE rent_slot__rent.rent_id = :rentId
            """,
            parameters, rowMapper
        )
    }

}
