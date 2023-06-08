package moscow.createdin.backend.service

import moscow.createdin.backend.model.domain.Coordinates
import moscow.createdin.backend.model.dto.CoordinatesDTO
import moscow.createdin.backend.model.entity.CoordinatesEntity
import moscow.createdin.backend.repository.CoordinatesRepository
import org.springframework.stereotype.Service

@Service
class CoordinatesService(private val coordinatesRepository: CoordinatesRepository) {

    fun saveCoordinates(coordinates: CoordinatesDTO): Coordinates {
        val coordinatesEntity = CoordinatesEntity(
            latitude = coordinates.latitude,
            longitude = coordinates.longitude
        )
        val coordinatesId: Long = coordinatesRepository.save(coordinatesEntity)

        return Coordinates(
            id = coordinatesId,
            latitude = coordinates.latitude,
            longitude = coordinates.longitude
        )
    }

    fun deleteCoordinates(id: Long) {
        coordinatesRepository.delete(id)
    }
}
