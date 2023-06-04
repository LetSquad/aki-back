package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.place.PlaceReview
import moscow.createdin.backend.model.dto.place.PlaceReviewDTO
import moscow.createdin.backend.model.entity.PlaceReviewEntity
import moscow.createdin.backend.model.enums.PlaceConfirmationStatus
import org.springframework.stereotype.Component

@Component
class PlaceReviewMapper(
    private val rentMapper: RentMapper,
) {
    fun domainToEntity(placeReview: PlaceReview) = PlaceReviewEntity(
        id = placeReview.id,
        rent = placeReview.rent,
        rating = placeReview.rating,
        reviewText = placeReview.reviewText,

        status = placeReview.status.name,
        banReason = placeReview.banReason,
        admin = placeReview.admin
    )

    fun entityToDomain(placeReview: PlaceReviewEntity) = PlaceReview(
        id = placeReview.id,
        rent = placeReview.rent,
        rating = placeReview.rating,
        reviewText = placeReview.reviewText,

        status = PlaceConfirmationStatus.valueOf(placeReview.status),
        banReason = placeReview.banReason,
        admin = placeReview.admin
    )

    fun domainToDto(placeReview: PlaceReview) = PlaceReviewDTO(
        id = placeReview.id,
        rent = placeReview.rent,
        rating = placeReview.rating,
        reviewText = placeReview.reviewText,

        status = placeReview.status,
        banReason = placeReview.banReason,
        admin = placeReview.admin
    )
}
