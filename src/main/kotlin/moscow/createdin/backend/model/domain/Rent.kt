package moscow.createdin.backend.model.domain

import moscow.createdin.backend.model.enums.AdminStatusType

data class Rent(
    val id: Long?,

    val place: Place,
    val renter: AkiUser,
    val rentSlots: List<RentSlot>,

    val status: AdminStatusType,
    val banReason: String?,
    val admin: Long?,
)
