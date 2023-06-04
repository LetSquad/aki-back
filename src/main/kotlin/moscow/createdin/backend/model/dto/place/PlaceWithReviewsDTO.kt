package moscow.createdin.backend.model.dto.place

import moscow.createdin.backend.model.domain.place.PlaceReview
import moscow.createdin.backend.model.dto.RatingDTO
import moscow.createdin.backend.model.enums.PlaceConfirmationStatus
import moscow.createdin.backend.model.enums.SpecializationType

data class PlaceWithReviewsDTO(
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
    val minCapacity: Int?,
    val maxCapacity: Int?,
    val levelNumber: Int?,
    val parking: Boolean?,


    val price: PlacePriceDTO,
    val status: PlaceConfirmationStatus,

    val services: List<PlaceServiceDTO>?,
    val equipments: List<PlaceEquipmentDTO>?,
    val facilities: List<PlaceFacilitiesDTO>?,
    val placeImages: List<String>?,

    val user: PlaceUserDTO,
    val rating: RatingDTO,
    val admin: Long?,

    val favorite: Boolean,
    val reviews: List<PlaceReview>
)
