package moscow.createdin.backend.model.entity

import java.math.BigDecimal
import java.sql.Date

data class RentSlotEntity(
    val id: Long?,

    val place: PlaceEntity?,

    val timeStart: Date,
    val timeEnd: Date,

    val status: String,
    val price: BigDecimal,
)
