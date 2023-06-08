package moscow.createdin.backend.model.dto

import moscow.createdin.backend.model.enums.RentSlotStatus
import java.math.BigDecimal
import java.sql.Timestamp

data class RentSlotDTO(
    val id: Long?,
    val placeId: Long?,

    val timeStart: Timestamp,
    val timeEnd: Timestamp,

    val status: RentSlotStatus?,
    val price: BigDecimal
)
