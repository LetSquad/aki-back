package moscow.createdin.backend.service

import moscow.createdin.backend.getLogger
import moscow.createdin.backend.mapper.PlaceReviewMapper
import moscow.createdin.backend.model.dto.RentReviewDTO
import moscow.createdin.backend.model.dto.place.PlaceReviewDTO
import moscow.createdin.backend.model.entity.PlaceReviewEntity
import moscow.createdin.backend.model.enums.PlaceConfirmationStatus
import moscow.createdin.backend.repository.PlaceReviewRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlaceReviewService(
    private val placeReviewRepository: PlaceReviewRepository,
    private val placeReviewMapper: PlaceReviewMapper
) {

    @Transactional
    fun review(id: Long, rentReviewDTO: RentReviewDTO): List<PlaceReviewDTO> {
        //TODO тут надо подумать насчет того, что делать с арендой, если ее брать из rentService получается циклическая зависимость
        placeReviewRepository.save(
            PlaceReviewEntity(
                null,
                rentReviewDTO.rentId,
                reviewText = rentReviewDTO.text,
                rating = rentReviewDTO.rating,
                admin = null,
                banReason = null,
                status = PlaceConfirmationStatus.UNVERIFIED.name
            )
        )
        return findByPlaceId(id)
    }

    fun findByPlaceId(placeId: Long): List<PlaceReviewDTO> {
        return placeReviewRepository.findByPlaceId(placeId)
            .map { placeReviewMapper.entityToDomain(it) }
            .map { placeReviewMapper.domainToDto(it) }
    }

    companion object {
        private val log = getLogger<UserService>()
    }
}
