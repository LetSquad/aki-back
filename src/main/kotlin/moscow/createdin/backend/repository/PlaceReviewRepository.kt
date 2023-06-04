package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.PlaceReviewEntity

interface PlaceReviewRepository {

    fun save(placeReviewEntity: PlaceReviewEntity): Long

    fun findByPlaceId(placeId: Long): List<PlaceReviewEntity>
}
