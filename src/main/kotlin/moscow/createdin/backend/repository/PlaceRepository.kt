package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.PlaceEntity
import moscow.createdin.backend.model.enums.PlaceSortDirection
import moscow.createdin.backend.model.enums.PlaceSortType
import moscow.createdin.backend.model.enums.SpecializationType
import java.sql.Timestamp

interface PlaceRepository {

    fun countByFilter(
        specialization: List<SpecializationType>,
        rating: Int?,
        priceMin: Int?,
        priceMax: Int?,
        capacityMin: Int?,
        capacityMax: Int?,
        squareMin: Int?,
        squareMax: Int?,
        levelNumberMin: Int?,
        levelNumberMax: Int?,
        withParking: Boolean?,
        dateFrom: Timestamp?,
        dateTo: Timestamp?
    ): Int

    fun findAll(
        specialization: List<SpecializationType>,
        rating: Int?,
        priceMin: Int?,
        priceMax: Int?,
        capacityMin: Int?,
        capacityMax: Int?,
        squareMin: Int?,
        squareMax: Int?,
        levelNumberMin: Int?,
        levelNumberMax: Int?,
        withParking: Boolean?,
        dateFrom: Timestamp?,
        dateTo: Timestamp?,

        pageNumber: Long,
        limit: Int,
        sortType: PlaceSortType,
        sortDirection: PlaceSortDirection
    ): List<PlaceEntity>

    fun save(place: PlaceEntity): Long

    fun findById(id: Long): PlaceEntity

    fun findByUserId(
        pageNumber: Long,
        limit: Int,
        userId: Long
    ): List<PlaceEntity>

    fun countByUserId(userId: Long): Int

    fun findUnverified(
        pageNumber: Long,
        limit: Int
    ): List<PlaceEntity>

    fun countUnverified(): Int

    fun update(place: PlaceEntity)
}
