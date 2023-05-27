package moscow.createdin.backend.model.dto.place

import moscow.createdin.backend.model.enums.PlaceConfirmationStatus
import moscow.createdin.backend.model.enums.SpecializationType

data class PlaceDTO(
    val id: Long,

    val name: String,
    val address: String,
    val email: String,
    val site: String?,
    val specialization: List<SpecializationType>,
    val phone: String,
    val description: String,
    val fullSquare: Int,
    val freeSquare: Int,
    val capacityMin: Int,
    val capacityMax: Int,
    val levelNumber: Int?,
    val parking: Boolean?,

    val price: PlacePriceDTO,
    val status: PlaceConfirmationStatus,

    val services: List<PlaceServiceDTO>?,
    val equipments: List<PlaceEquipmentDTO>?,
    val facilities: List<PlaceFacilitiesDTO>?,
    val placeImages: List<String>?,

    val user: PlaceUserDTO,
    val admin: Long?
)
