package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.RentSlot
import moscow.createdin.backend.model.entity.RentSlotEntity
import moscow.createdin.backend.model.enums.RentSlotStatusType
import org.springframework.stereotype.Component

@Component
class RentSlotMapper(
    private val placeMapper: PlaceMapper
) {
    fun domainToEntity(rentSlot: RentSlot) = RentSlotEntity(
        id = rentSlot.id,
        place = rentSlot.place?.let { placeMapper.domainToEntity(rentSlot.place) },
        timeStart = rentSlot.timeStart,
        timeEnd = rentSlot.timeEnd,
        status = rentSlot.status.name,
        price = rentSlot.price,
    )

    fun entityToDomain(rentSlot: RentSlotEntity) = RentSlot(
        id = rentSlot.id,
        place = rentSlot.place?.let { placeMapper.entityToDomain(rentSlot.place) },
        timeStart = rentSlot.timeStart,
        timeEnd = rentSlot.timeEnd,
        status = RentSlotStatusType.valueOf(rentSlot.status),
        price = rentSlot.price,
    )
}
