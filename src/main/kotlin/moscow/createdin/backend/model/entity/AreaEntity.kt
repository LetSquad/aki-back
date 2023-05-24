package moscow.createdin.backend.model.entity

data class AreaEntity(
    val id: Long?,
    val user: AkiUserEntity,

    val name: String,
    val description: String,
    val areaImage: String?,
    val logoImage: String?,
    val address: String,
    val website: String?,
    val email: String?,
    val phone: String?,

    val coordinates: CoordinatesEntity?,

    val status: String,

    val banReason: String?,
    val admin: Long?
)
