package moscow.createdin.backend.model.domain.place

import moscow.createdin.backend.model.enums.PriceType

data class PlacePrice(
    val price: Double?,
    val priceType: PriceType?
)
