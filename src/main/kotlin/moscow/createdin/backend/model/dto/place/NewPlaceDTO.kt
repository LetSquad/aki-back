package moscow.createdin.backend.model.dto.place

import moscow.createdin.backend.model.enums.SpecializationType

data class NewPlaceDTO(

    val name: String,
    val type: String,
    val address: String,
    val email: String,
    val site: String?,
    val specialization: SpecializationType,
    val phone: String,
    val description: String,
    val fullSquare: Int,
    val freeSquare: Int,
    val capacityMin: Int,
    val capacityMax: Int,
    val levelNumber: Int?,
    val parking: Boolean?,

    val services: List<PlaceServiceDTO>?,
    val equipments: List<PlaceEquipmentDTO>?,
    val facilities: List<PlaceFacilitiesDTO>?,
    val rules: String?,
    val accessibility: String?,

    val admin: Long?,
    val coordinates: Long?,
    val area: Long
)
