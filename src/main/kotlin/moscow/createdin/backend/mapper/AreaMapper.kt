package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.Area
import moscow.createdin.backend.model.entity.AreaEntity
import moscow.createdin.backend.model.enums.AdminStatusType
import org.springframework.stereotype.Component

@Component
class AreaMapper(
    private val akiUserAdminMapper: AkiUserAdminMapper,
    private val coordinatesMapper: CoordinatesMapper
) {
    fun domainToEntity(area: Area) = AreaEntity(
        id = area.id,
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
        admin = area.admin?.let { akiUserAdminMapper.domainToEntity(area.admin) },
    )

    fun entityToDomain(area: AreaEntity) = Area(
        id = area.id,
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
        admin = area.admin?.let { akiUserAdminMapper.entityToDomain(area.admin) }
    )
}
