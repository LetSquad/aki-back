package moscow.createdin.backend.model.entity

import moscow.createdin.backend.model.enums.SpecializationType
import org.postgresql.util.PGobject

data class PlaceEntity(
    val id: Long?,

    val user: AkiUserEntity,
    val area: AreaEntity?,
    val coordinates: CoordinatesEntity?,

    val type: String?,
    val name: String,
    val specialization: List<SpecializationType>,
    val description: String,
    val address: String,
    val phone: String,
    val email: String,
    val website: String?,
    val levelNumber: Int?,
    val fullArea: Int,
    val rentableArea: Int,
    val minCapacity: Int?,
    val maxCapacity: Int?,
    val parking: Boolean?,

    val services: PGobject?,
    val rules: String?, // jsonb
    val accessibility: String?, // jsonb
    val facilities: PGobject?,
    val equipments: PGobject?,

    val status: String,
    val banReason: String?,
    val admin: Long?,
    val minPrice: Double?,
    val priceType: String?,
    val rating: Double?,
    val rateCount: Int?,
    val favorite: Boolean,
    val metroStations: PGobject?
)
