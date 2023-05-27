package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.PlaceImageEntity

interface PlaceImageRepository {
    fun findByPlaceId(
        placeId: Long?
    ): List<PlaceImageEntity>
}
