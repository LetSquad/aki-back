package moscow.createdin.backend.model.entity

data class PlaceImageEntity(
    val id: Long?,

    val placeId: Long,
    val image: String,
    val priority: Int
)
