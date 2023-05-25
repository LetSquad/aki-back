package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.Coordinates
import moscow.createdin.backend.model.dto.CoordinatesDTO
import moscow.createdin.backend.model.entity.CoordinatesEntity
import org.springframework.stereotype.Component

@Component
class CoordinatesMapper {
    fun domainToEntity(coordinates: Coordinates) = CoordinatesEntity(
        id = coordinates.id,

        longitude = coordinates.longitude,
        latitude = coordinates.latitude,
    )

    fun entityToDomain(coordinates: CoordinatesEntity) = Coordinates(
        id = coordinates.id,

        longitude = coordinates.longitude,
        latitude = coordinates.latitude,
    )

    fun dtoToDomain(coordinates: CoordinatesDTO) = Coordinates(
        id = coordinates.id,

        longitude = coordinates.longitude,
        latitude = coordinates.latitude,
    )

    fun domainToDto(coordinates: Coordinates) = CoordinatesDTO(
        id = coordinates.id,

        longitude = coordinates.longitude,
        latitude = coordinates.latitude,
    )
}
