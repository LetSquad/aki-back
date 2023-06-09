package moscow.createdin.backend.model.entity

import java.math.BigDecimal
import java.sql.Timestamp

data class RentSlotEntity(
    val id: Long?,
    val placeId: Long?,

    val timeStart: Timestamp,
    val timeEnd: Timestamp,

    val status: String,
    val price: BigDecimal
)
