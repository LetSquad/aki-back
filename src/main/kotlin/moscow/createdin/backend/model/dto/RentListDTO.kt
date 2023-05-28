package moscow.createdin.backend.model.dto

data class RentListDTO(
    val rents: List<RentDTO>,
    val total: Int
)
