package moscow.createdin.backend.model.dto

data class CreateRentRequestDTO(
    val placeId: Long,
    val rentSlotIds: List<Long>,
)
