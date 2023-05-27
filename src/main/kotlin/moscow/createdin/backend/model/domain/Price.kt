package moscow.createdin.backend.model.domain

import moscow.createdin.backend.model.enums.PriceType

data class Price(
    val price: Double?,
    val priceType: PriceType
)
