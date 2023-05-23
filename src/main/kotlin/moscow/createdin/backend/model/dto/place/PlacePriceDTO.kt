package moscow.createdin.backend.model.dto.place

import moscow.createdin.backend.model.enums.PriceType

data class PlacePriceDTO(
    val price: Double?,
    val priceType: PriceType?
)
