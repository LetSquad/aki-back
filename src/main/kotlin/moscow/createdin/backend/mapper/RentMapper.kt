package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.Rent
import moscow.createdin.backend.model.entity.RentEntity
import moscow.createdin.backend.model.enums.AdminStatusType
import org.springframework.stereotype.Component

@Component
class RentMapper(
    private val akiUserAdminMapper: AkiUserAdminMapper,
    private val userMapper: UserMapper,
    private val placeMapper: PlaceMapper
) {
    fun domainToEntity(rent: Rent) = RentEntity(
        id = rent.id,
        place = rent.place?.let { placeMapper.domainToEntity(rent.place) },
        user = rent.user?.let { userMapper.domainToEntity(rent.user) },

        status = rent.status.name,
        banReason = rent.banReason,
        admin = rent.admin?.let { akiUserAdminMapper.domainToEntity(rent.admin) },
    )

    fun entityToDomain(rent: RentEntity) = Rent(
        id = rent.id,
        place = rent.place?.let { placeMapper.entityToDomain(rent.place) },
        user = rent.user?.let { userMapper.entityToDomain(rent.user) },

        status = AdminStatusType.valueOf(rent.status),
        banReason = rent.banReason,
        admin = rent.admin?.let { akiUserAdminMapper.entityToDomain(rent.admin) },
    )
}
