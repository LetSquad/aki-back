package moscow.createdin.backend.model.domain

import moscow.createdin.backend.model.enums.AdminStatusType

data class Area(
    val id: Long?,

    val name: String,
    val description: String,
    val areaImage: String?,
    val address: String,
    val website: String?,
    val email: String?,
    val phone: String?,

    val coordinates: Coordinates?,

    val status: AdminStatusType,
    val banReason: String?,
    val admin: AkiUser?,
)
