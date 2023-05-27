package moscow.createdin.backend.model.dto.place

import moscow.createdin.backend.model.enums.SpecializationType

data class UpdatePlaceDTO(
    val id: Long,
    val name: String,
    val email: String,
    val site: String?,
    val specialization: SpecializationType,
    val phone: String,
    val description: String,
    val freeSquare: Int,
    val capacityMin: Int,
    val capacityMax: Int,
    val parking: Boolean?,

    val services: List<PlaceServiceDTO>?,
    val equipments: List<PlaceEquipmentDTO>?,
    val facilities: List<PlaceFacilitiesDTO>?,
    val rules: String?,
    val accessibility: String?,

    val admin: Long?
)
