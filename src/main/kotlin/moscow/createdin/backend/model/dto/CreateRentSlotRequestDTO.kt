package moscow.createdin.backend.model.dto

import java.math.BigDecimal
import java.sql.Timestamp

data class CreateRentSlotRequestDTO(
    val price: BigDecimal,
    val timeStart: Timestamp,
    val timeEnd: Timestamp,
    val placeId: Long,
)
