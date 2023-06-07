package moscow.createdin.backend.model.dto

data class AkiUserUpdateDTO(
    val id: Long,

    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val phone: String?,

    val userImage: String?,
    val organizationLogo: String?,
    val inn: String?,
    val organization: String?,
    val jobTitle: String?
)
