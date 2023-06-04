package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.PlaceReviewEntity

interface PlaceReviewRepository {

    fun save(rentId: Long, rating: Int): Long

    fun findByPlaceId(placeId: Long): List<PlaceReviewEntity>
}
