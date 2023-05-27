package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.Place
import moscow.createdin.backend.model.dto.PlaceDTO
import moscow.createdin.backend.model.entity.PlaceEntity
import moscow.createdin.backend.model.enums.AdminStatusType
import org.springframework.stereotype.Component

@Component
class PlaceMapper(
    private val userMapper: UserMapper,
    private val areaMapper: AreaMapper,
    private val coordinatesMapper: CoordinatesMapper
) {
    fun domainToDto(place: Place) = PlaceDTO(
        id = place.id!!
    )

    fun domainToEntity(place: Place) = PlaceEntity(
        id = place.id,
        user = userMapper.domainToEntity(place.user),
        area = place.area?.let { areaMapper.domainToEntity(place.area) },
        coordinates = place.coordinates?.let { coordinatesMapper.domainToEntity(place.coordinates) },

        type = place.type,
        name = place.name,
        specialization = place.specialization,
        description = place.description,
        address = place.address,
        phone = place.phone,
        email = place.email,
        website = place.website,
        levelNumber = place.levelNumber,
        fullArea = place.fullArea,
        rentableArea = place.rentableArea,
        capacityMin = place.capacityMin,
        capacityMax = place.capacityMax,

        services = place.services,
        rules = place.rules,
        accessibility = place.accessibility,
        facilities = place.facilities,
        equipments = place.equipments,

        status = place.status.name,
        banReason = place.banReason,
        admin = place.admin
    )

    fun entityToDomain(place: PlaceEntity) = Place(
        id = place.id,
        user = userMapper.entityToDomain(place.user),
        area = place.area?.let { areaMapper.entityToDomain(place.area) },
        coordinates = place.coordinates?.let { coordinatesMapper.entityToDomain(place.coordinates) },

        type = place.type,
        name = place.name,
        specialization = place.specialization,
        description = place.description,
        address = place.address,
        phone = place.phone,
        email = place.email,
        website = place.website,
        levelNumber = place.levelNumber,
        fullArea = place.fullArea,
        rentableArea = place.rentableArea,
        capacityMin = place.capacityMin,
        capacityMax = place.capacityMax,

        services = place.services,
        rules = place.rules,
        accessibility = place.accessibility,
        facilities = place.facilities,
        equipments = place.equipments,

        status = AdminStatusType.valueOf(place.status),
        banReason = place.banReason,
        admin = place.admin
    )
}
