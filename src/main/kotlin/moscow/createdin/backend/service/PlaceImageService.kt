package moscow.createdin.backend.service

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
}
