package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.RentSlot
import moscow.createdin.backend.model.dto.CreateRentSlotRequestDTO
import moscow.createdin.backend.model.dto.RentSlotDTO
import moscow.createdin.backend.model.entity.RentSlotEntity
import moscow.createdin.backend.model.enums.RentSlotStatus
import org.springframework.stereotype.Component
import java.sql.Timestamp

@Component
class RentSlotMapper {
    fun domainToEntity(rentSlot: RentSlot) = RentSlotEntity(
        id = rentSlot.id,
        placeId = rentSlot.placeId,
        timeStart = Timestamp.from(rentSlot.timeStart),
        timeEnd = Timestamp.from(rentSlot.timeEnd),
        status = rentSlot.status.name,
        price = rentSlot.price
    )

    fun createDtoToDomain(rentSlot: CreateRentSlotRequestDTO) = RentSlot(
        id = null,
        placeId = rentSlot.placeId,
        timeStart = rentSlot.timeStart.toInstant(),
        timeEnd = rentSlot.timeEnd.toInstant(),
        status = RentSlotStatus.OPEN,
        price = rentSlot.price
    )

    fun entityToDomain(rentSlot: RentSlotEntity) = RentSlot(
        id = rentSlot.id,
        placeId = rentSlot.placeId,
        timeStart = rentSlot.timeStart.toInstant(),
        timeEnd = rentSlot.timeEnd.toInstant(),
        status = RentSlotStatus.valueOf(rentSlot.status),
        price = rentSlot.price
    )

    fun domainToDto(rentSlot: RentSlot) = RentSlotDTO(
        id = rentSlot.id,
        placeId = rentSlot.placeId,
        timeStart = Timestamp.from(rentSlot.timeStart),
        timeEnd = Timestamp.from(rentSlot.timeEnd),
        status = rentSlot.status,
        price = rentSlot.price
    )
}
