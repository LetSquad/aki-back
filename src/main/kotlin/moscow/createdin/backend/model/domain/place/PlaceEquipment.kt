package moscow.createdin.backend.model.domain.place

data class PlaceEquipment(
    val name: String,
    val price: PlacePrice,
    val count: Int?
)
