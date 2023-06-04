package moscow.createdin.backend.model.dto

data class RentReviewDTO(
    val rentId: Long,
    val rating: Int,
    val text: String
)
