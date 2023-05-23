package moscow.createdin.backend.model.domain.place

import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.domain.Rent
import moscow.createdin.backend.model.enums.AdminStatusType

data class PlaceReview(
    val id: Long?,

    val rent: Rent,
    val rating: Int,
    val reviewText: String,

    val status: AdminStatusType,
    val banReason: String?,
    val admin: Long?,
)
