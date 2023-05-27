package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.RentSlotEntity
import moscow.createdin.backend.repository.mapper.RentSlotRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository


@Repository
class RentSlotJdbc(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val rowMapper: RentSlotRowMapper
) : RentSlotRepository {

    override fun findById(id: Long): RentSlotEntity {
        val parameters = MapSqlParameterSource()
            .addValue("id", id)

        return jdbcTemplate.queryForObject(
            """
                $SQL_SELECT_ENTITY
                WHERE rent_slot.id = :id
            """, parameters, rowMapper
        )!!
    }

    override fun findByPlaceId(placeId: Long): List<RentSlotEntity> {
        val parameters = MapSqlParameterSource()
            .addValue("placeId", placeId)

        return jdbcTemplate.query(
            """
                $SQL_SELECT_ENTITY
                WHERE rent_slot.place_id = :placeId
            """,
            parameters, rowMapper
        )
    }

    override fun save(rentSlots: List<RentSlotEntity>) {
        val parameters = rentSlots.map {
            MapSqlParameterSource()
                .addValue("placeId", it.placeId)
                .addValue("timeStart", it.timeStart)
                .addValue("timeEnd", it.timeEnd)
                .addValue("status", it.status)
                .addValue("price", it.price)
        }

        jdbcTemplate.batchUpdate(
            """
                INSERT 
                    INTO rent_slot (place_id, time_start, time_end, rent_slot_status, price) 
                VALUES (:placeId, :timeStart, :timeEnd, :status, :price)
                """,
            parameters.toTypedArray()
        )
    }

    override fun update(rentSlots: List<RentSlotEntity>) {
        val parameters = rentSlots.map {
            MapSqlParameterSource()
                .addValue("id", it.id)
                .addValue("status", it.status)
        }

        jdbcTemplate.batchUpdate(
            """
                UPDATE rent_slot 
                SET rent_slot_status = :status
                WHERE rent_slot.id = :id
            """,
            parameters.toTypedArray()
        )
    }

    companion object {
        private const val SQL_SELECT_ENTITY =
            """
                SELECT rent_slot.id,
                       rent_slot.place_id, 
                       rent_slot.price, 
                       rent_slot.time_start, 
                       rent_slot.time_end, 
                       rent_slot.rent_slot_status, 
                
                       place.id
                FROM rent_slot
                         INNER JOIN place on rent_slot.place_id = place.id
            """
    }
}
