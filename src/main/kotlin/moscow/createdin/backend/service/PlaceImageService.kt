package moscow.createdin.backend.service

import moscow.createdin.backend.model.entity.PlaceImageEntity
import moscow.createdin.backend.repository.PlaceImageRepository
import org.springframework.stereotype.Service

@Service
class PlaceImageService(
    private val placeImageRepository: PlaceImageRepository
) {

    fun getPlaceImages(
        placeId: Long?
    ): List<String> {
        return placeImageRepository.findByPlaceId(placeId)
            .map { it.image }
    }

    fun savePlaceImage(placeId: Long, image: String, priority: Int) {
        placeImageRepository.save(PlaceImageEntity(null, placeId, image, priority))
    }

    fun clearPlaceImages(placeId: Long, images: List<String>?) {
        placeImageRepository.deleteByPlaceId(placeId, if (images == null || images.isEmpty()) listOf("") else images)
    }
}
