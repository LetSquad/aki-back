package moscow.createdin.backend.model.entity

data class RentEntity(
    val id: Long?,

    val place: PlaceEntity?,
    val user: AkiUserEntity?,

    val status: String,

    val banReason: String?,
    val admin: AkiUserEntity?,
)
