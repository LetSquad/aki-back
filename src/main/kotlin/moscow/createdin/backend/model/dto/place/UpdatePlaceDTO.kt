package moscow.createdin.backend.model.dto.place

import moscow.createdin.backend.model.dto.CoordinatesDTO
import moscow.createdin.backend.model.enums.SpecializationType

data class UpdatePlaceDTO(
    val id: Long,
    val name: String,
    val email: String,
    val site: String?,
    val specialization: List<SpecializationType>,
    val phone: String,
    val description: String,
    val freeSquare: Int,
    val minCapacity: Int?,
    val maxCapacity: Int?,
    val parking: Boolean?,

    val coordinates: CoordinatesDTO?,

    val placeImages: List<String>?,
    val services: List<PlaceServiceDTO>?,
    val equipments: List<PlaceEquipmentDTO>?,
    val facilities: List<PlaceFacilitiesDTO>?,
    val rules: String?,
    val accessibility: String?,

    val admin: Long?
)
