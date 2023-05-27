package moscow.createdin.backend.model.domain.place

import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.domain.Price
import moscow.createdin.backend.model.enums.PlaceConfirmationStatus
import moscow.createdin.backend.model.enums.SpecializationType

data class SimplePlace(
    val id: Long?,

    val user: AkiUser,
    val area: Long?,
    val coordinates: Long?,

    val type: String,
    val name: String,
    val specialization: SpecializationType,
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

    val services: List<PlaceService>?,
    val rules: String?,
    val accessibility: String?,
    val facilities: List<PlaceFacility>?,
    val equipments: List<PlaceEquipment>?,

    val status: PlaceConfirmationStatus,
    val banReason: String?,
    val admin: Long?,
    val price: Price?
)
