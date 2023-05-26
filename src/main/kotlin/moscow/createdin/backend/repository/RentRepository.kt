package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.RentEntity

interface RentRepository {

    fun findById(id: Long): RentEntity

    fun update(rent: RentEntity)

    fun findByRenterId(id: Long): List<RentEntity>

    fun create(
        placeId: Long,
        renterId: Long
    ): Long

}
