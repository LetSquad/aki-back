package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.RentSlotEntity

interface RentSlotToRentRepository {

    fun create(
        rentId: Long,
        rentSlotIds: List<Long>)

    fun delete(rentId: Long)

    fun findSlotsByRentId(rentId: Long): List<RentSlotEntity>
}
