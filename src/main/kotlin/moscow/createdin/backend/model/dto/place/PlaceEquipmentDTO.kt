package moscow.createdin.backend.model.dto.place

data class PlaceEquipmentDTO(
    val name: String,
    val price: PlacePriceDTO,
    val count: Int?
)
