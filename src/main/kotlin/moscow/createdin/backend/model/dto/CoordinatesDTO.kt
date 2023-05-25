package moscow.createdin.backend.model.dto

import java.math.BigDecimal

data class CoordinatesDTO(
    val id: Long?,

    val longitude: BigDecimal,
    val latitude: BigDecimal
)
