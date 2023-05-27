package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.PlaceReview
import moscow.createdin.backend.model.entity.PlaceReviewEntity
import moscow.createdin.backend.model.enums.AdminStatusType
import org.springframework.stereotype.Component

@Component
class PlaceReviewMapper(
    private val rentMapper: RentMapper,
) {
    fun domainToEntity(placeReview: PlaceReview) = PlaceReviewEntity(
        id = placeReview.id,
        rent = rentMapper.domainToEntity(placeReview.rent),
        rating = placeReview.rating,
        reviewText = placeReview.reviewText,

        status = placeReview.status.name,
        banReason = placeReview.banReason,
        admin = placeReview.admin
    )

    fun entityToDomain(placeReview: PlaceReviewEntity) = PlaceReview(
        id = placeReview.id,
        rent = rentMapper.entityToDomain(placeReview.rent, listOf()),
        rating = placeReview.rating,
        reviewText = placeReview.reviewText,

        status = AdminStatusType.valueOf(placeReview.status),
        banReason = placeReview.banReason,
        admin = placeReview.admin
    )
}
