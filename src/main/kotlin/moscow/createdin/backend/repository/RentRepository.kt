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

    fun findByRenterId(
        pageNumber: Long,
        limit: Int,
        userId: Long
    ): List<RentEntity>
    fun countByRenterId(userId: Long): Int

}
