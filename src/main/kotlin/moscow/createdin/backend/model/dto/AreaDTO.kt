package moscow.createdin.backend.model.dto

import moscow.createdin.backend.model.enums.AdminStatusType

data class AreaDTO(
    val id: Long?,
    val user: AkiUserDTO,

    val name: String,
    val description: String,
    val areaImage: String?,
    val address: String,
    val website: String?,
    val email: String?,
    val phone: String?,

    val coordinates: CoordinatesDTO?,

    val status: AdminStatusType,
    val banReason: String?,
    val admin: AkiUserDTO?
)
