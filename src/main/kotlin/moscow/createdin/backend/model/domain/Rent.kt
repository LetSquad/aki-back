package moscow.createdin.backend.model.domain

import moscow.createdin.backend.model.domain.place.Place
import moscow.createdin.backend.model.enums.RentConfirmationStatus

data class Rent(
    val id: Long?,

    val place: Place,
    val renter: AkiUser,
    val rentSlots: List<RentSlot>,

    val status: RentConfirmationStatus,
    val agreement: String?,
    val banReason: String?,
    val admin: Long?,
)
