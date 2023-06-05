package moscow.createdin.backend.model.dto

import moscow.createdin.backend.model.dto.place.PlaceDTO
import moscow.createdin.backend.model.enums.RentConfirmationStatus

data class RentDTO(
    val id: Long,

    val place: PlaceDTO,
    val renter: AkiUserDTO,
    val rentSlots: List<RentSlotDTO>,

    val status: RentConfirmationStatus,
    val agreement: String?,
    val banReason: String?,
    val adminId: Long?
)
