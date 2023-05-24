package moscow.createdin.backend.model.dto

data class CreateAreaRequestDTO(
    val name: String,
    val description: String,
    val areaImage: String?,
    val address: String,
    val website: String?,
    val email: String?,
    val phone: String?,

    val coordinates: CoordinatesDTO?
)
