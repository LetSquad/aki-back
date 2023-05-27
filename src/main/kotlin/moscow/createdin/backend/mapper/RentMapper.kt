package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.Rent
import moscow.createdin.backend.model.domain.RentSlot
import moscow.createdin.backend.model.dto.RentDTO
import moscow.createdin.backend.model.entity.RentEntity
import moscow.createdin.backend.model.enums.RentConfirmationStatus
import org.springframework.stereotype.Component

@Component
class RentMapper(
    private val rentSlotMapper: RentSlotMapper,
    private val userMapper: UserMapper,
    private val placeMapper: PlaceMapper
) {
    fun domainToEntity(rent: Rent) = RentEntity(
        id = rent.id,

        place = placeMapper.domainToEntity(rent.place),
        user = userMapper.domainToEntity(rent.renter),

        rentSlotIds = rent.rentSlots.map { it.id!! },
        status = rent.status.name,
        banReason = rent.banReason,
        admin = rent.admin
    )

    fun domainToDto(rent: Rent) = RentDTO(
        id = rent.id!!,
        place = placeMapper.domainToDto(rent.place, listOf()),
        renter = userMapper.domainToDto(rent.renter),

        rentSlots = rent.rentSlots.map { rentSlotMapper.domainToDto(it) },
        status = rent.status,
        banReason = rent.banReason,
        adminId = rent.admin
    )

    fun entityToDomain(rent: RentEntity, rentSlots: List<RentSlot>) = Rent(
        id = rent.id,
        place = placeMapper.entityToDomain(rent.place, listOf()),
        renter = userMapper.entityToDomain(rent.user),
        rentSlots = rentSlots,

        status = RentConfirmationStatus.valueOf(rent.status),
        banReason = rent.banReason,
        admin = rent.admin,
    )
}
