package moscow.createdin.backend.model.domain

import moscow.createdin.backend.model.enums.RentSlotStatusType
import java.math.BigDecimal
import java.sql.Date

data class RentSlot(
    val id: Long?,

    val place: Place?,

    val timeStart: Date,
    val timeEnd: Date,

    val status: RentSlotStatusType,
    val price: BigDecimal,
)
