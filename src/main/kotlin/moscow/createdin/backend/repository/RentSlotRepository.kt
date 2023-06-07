package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.RentSlotEntity

interface RentSlotRepository {

    fun findById(id: Long): RentSlotEntity

    fun findByPlaceId(placeId: Long): List<RentSlotEntity>

    fun save(rentSlots: List<RentSlotEntity>)

    fun update(rentSlots: List<RentSlotEntity>)

    fun findByPlaceIdFuture(placeId: Long): List<RentSlotEntity>

}
