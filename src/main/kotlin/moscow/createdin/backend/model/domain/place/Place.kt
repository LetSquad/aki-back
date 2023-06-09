package moscow.createdin.backend.model.domain.place

import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.domain.Area
import moscow.createdin.backend.model.domain.Coordinates
import moscow.createdin.backend.model.domain.Price
import moscow.createdin.backend.model.domain.RentSlot
import moscow.createdin.backend.model.enums.PlaceConfirmationStatus
import moscow.createdin.backend.model.enums.SpecializationType

data class Place(
    val id: Long?,

    val user: AkiUser,
    val area: Area?,
    val coordinates: Coordinates?,

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

    val services: List<PlaceService>?,
    val rules: String?,
    val accessibility: String?,
    val facilities: List<PlaceFacility>?,
    val equipments: List<PlaceEquipment>?,
    val metroStations: List<String>?,


    val status: PlaceConfirmationStatus,
    val banReason: String?,
    val admin: Long?,
    val price: Price?,
    val rentSlots: List<RentSlot>?,
    val rating: Double?,
    val rateCount: Int?,

    val favorite: Boolean
)
