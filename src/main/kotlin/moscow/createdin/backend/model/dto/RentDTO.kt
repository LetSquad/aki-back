package moscow.createdin.backend.model.dto

data class RentDTO(
    val id: Long,

    val place: PlaceDTO,
    val renter: AkiUserDTO,
    val rentSlots: List<RentSlotDTO>,

    val status: String,
    val banReason: String?,
    val adminId: Long?
)
