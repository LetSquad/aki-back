package moscow.createdin.backend.model.domain

import java.math.BigDecimal

data class Coordinates(
    val id: Long?,

    val longitude: BigDecimal,
    val latitude: BigDecimal,
)
