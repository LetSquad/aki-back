package moscow.createdin.backend.model.entity

import java.math.BigDecimal

data class CoordinatesEntity(
    val id: Long?,

    val longitude: BigDecimal,
    val latitude: BigDecimal,
)
