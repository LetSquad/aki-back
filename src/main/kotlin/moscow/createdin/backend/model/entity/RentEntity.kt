package moscow.createdin.backend.model.entity

data class RentEntity(
    val id: Long?,

    val place: PlaceEntity,
    val user: AkiUserEntity,
    val rentSlotIds: List<Long>,

    val status: String,
    val agreement: String?,
    val banReason: String?,
    val admin: Long?
)
