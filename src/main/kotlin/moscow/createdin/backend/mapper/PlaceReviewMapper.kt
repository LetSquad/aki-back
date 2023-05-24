package moscow.createdin.backend.mapper

import moscow.createdin.backend.model.domain.PlaceReview
import moscow.createdin.backend.model.entity.PlaceReviewEntity
import moscow.createdin.backend.model.enums.AdminStatusType
import org.springframework.stereotype.Component

@Component
class PlaceReviewMapper(
    private val rentMapper: RentMapper,
    private val akiUserAdminMapper: AkiUserAdminMapper
) {
    fun domainToEntity(placeReview: PlaceReview) = PlaceReviewEntity(
        id = placeReview.id,
        rent = placeReview.rent?.let { rentMapper.domainToEntity(placeReview.rent) },
        rating = placeReview.rating,
        reviewText = placeReview.reviewText,

        status = placeReview.status.name,
        banReason = placeReview.banReason,
        admin = placeReview.admin?.let { akiUserAdminMapper.domainToEntity(placeReview.admin) },
    )

    fun entityToDomain(placeReview: PlaceReviewEntity) = PlaceReview(
        id = placeReview.id,
        rent = placeReview.rent?.let { rentMapper.entityToDomain(placeReview.rent) },
        rating = placeReview.rating,
        reviewText = placeReview.reviewText,

        status = AdminStatusType.valueOf(placeReview.status),
        banReason = placeReview.banReason,
        admin = placeReview.admin?.let { akiUserAdminMapper.entityToDomain(placeReview.admin) },
    )
}
