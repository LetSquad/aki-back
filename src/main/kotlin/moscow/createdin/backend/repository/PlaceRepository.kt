package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.PlaceEntity
import moscow.createdin.backend.model.enums.PlaceSortDirection

interface PlaceRepository {

    fun countByFilter(
        specialization: String?, capacity: Int?, fullAreaMin: Int?, fullAreaMax: Int?, levelNumberMin: Int?,
        levelNumberMax: Int?, parking: Boolean?
    ): Int

    fun findAll(
        specialization: String?, capacity: Int?, fullAreaMin: Int?, fullAreaMax: Int?, levelNumberMin: Int?,
        levelNumberMax: Int?, parking: Boolean?, pageNumber: Long, limit: Int, sortType: String,
        sortDirection: PlaceSortDirection
    ): List<PlaceEntity>

    fun save(place: PlaceEntity): Long

    fun findById(id: Long): PlaceEntity
    fun update(place: PlaceEntity)
}
