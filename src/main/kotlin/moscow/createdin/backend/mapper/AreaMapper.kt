package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.domain.Area
import moscow.createdin.backend.model.dto.AreaDTO
import moscow.createdin.backend.model.dto.CreateAreaRequestDTO
import moscow.createdin.backend.model.entity.AreaEntity
import moscow.createdin.backend.model.enums.AdminStatusType
import org.springframework.stereotype.Component

@Component
class AreaMapper(
    private val userMapper: UserMapper,
    private val coordinatesMapper: CoordinatesMapper
) {
    fun domainToEntity(area: Area) = AreaEntity(
        id = area.id,
        user = userMapper.domainToEntity(area.user),
        name = area.name,
        description = area.description,
        areaImage = area.areaImage,
        address = area.address,
        website = area.website,
        email = area.email,
        phone = area.phone,
        coordinates = area.coordinates?.let { coordinatesMapper.domainToEntity(area.coordinates) },
        status = area.status.name,
        banReason = area.banReason,
        admin = area.admin
    )

    fun entityToDomain(area: AreaEntity) = Area(
        id = area.id,
        user = userMapper.entityToDomain(area.user),
        name = area.name,
        description = area.description,
        areaImage = area.areaImage,
        address = area.address,
        website = area.website,
        email = area.email,
        phone = area.phone,
        coordinates = area.coordinates?.let { coordinatesMapper.entityToDomain(area.coordinates) },
        status = AdminStatusType.valueOf(area.status),
        banReason = area.banReason,
        admin = area.admin
    )

    fun createDtoToDomain(createAreaRequestDTO: CreateAreaRequestDTO, akiUser: AkiUser) = Area(
        id = null,
        user = akiUser,
        name = createAreaRequestDTO.name,
        description = createAreaRequestDTO.description,
        areaImage = createAreaRequestDTO.areaImage,
        address = createAreaRequestDTO.address,
        website = createAreaRequestDTO.website,
        email = createAreaRequestDTO.email,
        phone = createAreaRequestDTO.phone,
        coordinates = createAreaRequestDTO.coordinates?.let { coordinatesMapper.dtoToDomain(createAreaRequestDTO.coordinates) },
        status = AdminStatusType.UNVERIFIED,
        banReason = null,
        admin = null
    )

    fun dtoToDomain(area: AreaDTO, akiUser: AkiUser) = Area(
        id = area.id,
        user = akiUser,
        name = area.name,
        description = area.description,
        areaImage = area.areaImage,
        address = area.address,
        website = area.website,
        email = area.email,
        phone = area.phone,
        coordinates = area.coordinates?.let { coordinatesMapper.dtoToDomain(area.coordinates) },
        status = AdminStatusType.UNVERIFIED,
        banReason = null,
        admin = null
    )

    fun domainToDto(area: Area) = AreaDTO(
        id = area.id,
        user = userMapper.domainToDto(area.user),
        name = area.name,
        description = area.description,
        areaImage = area.areaImage,
        address = area.address,
        website = area.website,
        email = area.email,
        phone = area.phone,
        coordinates = area.coordinates?.let { coordinatesMapper.domainToDto(area.coordinates) },
        status = area.status,
        banReason = area.banReason,
        admin = area.admin
    )

}
