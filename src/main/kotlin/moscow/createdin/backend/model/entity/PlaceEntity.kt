package moscow.createdin.backend.model.entity

data class PlaceEntity(
    val id: Long?,

    val user: AkiUserEntity,
    val area: AreaEntity?,
    val coordinates: CoordinatesEntity?,

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

    val status: String,
    val banReason: String?,
    val admin: Long?,
)
