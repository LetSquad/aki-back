package moscow.createdin.backend.model.entity

import java.math.BigDecimal

data class CoordinatesEntity(
    val id: Long? = null,

    val latitude: BigDecimal,
    val longitude: BigDecimal
)
