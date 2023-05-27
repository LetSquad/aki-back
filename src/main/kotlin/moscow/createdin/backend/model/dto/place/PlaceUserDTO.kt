package moscow.createdin.backend.model.dto.place

data class PlaceUserDTO(
    val id: Long,

    val email: String,
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val phone: String,

    val userImage: String?,
    val inn: String?,
    val organization: String?,
    val jobTitle: String?
)
