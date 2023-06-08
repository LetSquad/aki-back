package moscow.createdin.backend.model.domain

import moscow.createdin.backend.model.enums.RentSlotStatus
import java.math.BigDecimal
import java.time.Instant

data class RentSlot(
    val id: Long?,
    val placeId: Long?,

    val timeStart: Instant,
    val timeEnd: Instant,

    val status: RentSlotStatus,
    val price: BigDecimal
)
