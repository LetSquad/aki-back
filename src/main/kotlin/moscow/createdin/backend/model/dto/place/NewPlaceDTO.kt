package moscow.createdin.backend.model.dto.place

import moscow.createdin.backend.model.enums.SpecializationType

data class NewPlaceDTO(

    val name: String,
    val type: String?,
    val address: String,
    val email: String,
    val site: String?,
    val specialization: List<SpecializationType>,
    val phone: String,
    val description: String,
    val fullSquare: Int,
    val freeSquare: Int,
    val minCapacity: Int?,
    val maxCapacity: Int?,
    val levelNumber: Int?,
    val parking: Boolean?,

    val services: List<PlaceServiceDTO>?,
    val equipments: List<PlaceEquipmentDTO>?,
    val facilities: List<PlaceFacilitiesDTO>?,
    val metroStations: List<String>?,
    val rules: String?,
    val accessibility: String?,

    val admin: Long?,
    val coordinates: Long?,
    val area: Long?
)
