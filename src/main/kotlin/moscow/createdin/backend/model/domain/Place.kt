package moscow.createdin.backend.model.domain

import moscow.createdin.backend.model.enums.AdminStatusType

data class Place(
    val id: Long?,

    val user: AkiUser,
    val area: Area?,
    val coordinates: Coordinates?,

    val type: String,
    val name: String,
    val specialization: String,
    val description: String,
    val address: String,
    val phone: String,
    val email: String,
    val website: String?,
    val levelNumber: Int?,
    val fullArea: Int,
    val rentableArea: Int,
    val capacityMin: Int,
    val capacityMax: Int,

    val services: String?,  // jsonb
    val rules: String?, // jsonb
    val accessibility: String?, // jsonb
    val facilities: String?, // jsonb
    val equipments: String?, // jsonb

    val status: AdminStatusType,
    val banReason: String?,
    val admin: Long?,
)
