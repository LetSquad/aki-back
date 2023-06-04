package moscow.createdin.backend.model.dto.place

import moscow.createdin.backend.model.enums.PlaceConfirmationStatus

data class PlaceReviewDTO(
    val id: Long?,

    val rent: Long,
    val rating: Int,
    val reviewText: String,

    val status: PlaceConfirmationStatus,
    val banReason: String?,
    val admin: Long?,
)
