package moscow.createdin.backend.model.dto

import moscow.createdin.backend.model.enums.RentSlotStatusType
import java.math.BigDecimal
import java.sql.Timestamp

data class RentSlotDTO(
    val id: Long?,
    val placeId: Long?,

    val timeStart: Timestamp,
    val timeEnd: Timestamp,

    val status: RentSlotStatusType?,
    val price: BigDecimal
)
