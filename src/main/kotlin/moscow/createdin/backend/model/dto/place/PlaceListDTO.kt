package moscow.createdin.backend.model.dto.place

data class PlaceListDTO(
    val places: List<PlaceDTO>,
    val total: Int
)
