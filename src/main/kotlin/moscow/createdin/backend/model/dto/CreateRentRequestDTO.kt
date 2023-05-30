package moscow.createdin.backend.model.dto

data class CreateRentRequestDTO(
    val placeId: Long,
    val agreement: String,
    val rentSlotIds: List<Long>,
)
