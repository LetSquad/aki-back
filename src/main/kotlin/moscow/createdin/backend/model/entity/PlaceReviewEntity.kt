package moscow.createdin.backend.model.entity

data class PlaceReviewEntity(
    val id: Long?,

    val rent: RentEntity,
    val rating: Int,
    val reviewText: String,

    val status: String,

    val banReason: String?,
    val admin: AkiUserEntity?,
)