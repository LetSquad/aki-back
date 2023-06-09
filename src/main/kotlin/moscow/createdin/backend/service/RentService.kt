package moscow.createdin.backend.service

import moscow.createdin.backend.config.properties.AkiProperties
import moscow.createdin.backend.mapper.RentMapper
import moscow.createdin.backend.mapper.RentSlotMapper
import moscow.createdin.backend.model.domain.Rent
import moscow.createdin.backend.model.domain.RentSlot
import moscow.createdin.backend.model.dto.BanRequestDTO
import moscow.createdin.backend.model.dto.CreateRentRequestDTO
import moscow.createdin.backend.model.dto.RentDTO
import moscow.createdin.backend.model.dto.RentListDTO
import moscow.createdin.backend.model.dto.RentReviewDTO
import moscow.createdin.backend.model.dto.place.RentCancelRequestDTO
import moscow.createdin.backend.model.enums.RentConfirmationStatus
import moscow.createdin.backend.model.enums.RentSlotStatus
import moscow.createdin.backend.repository.PlaceReviewRepository
import moscow.createdin.backend.repository.RentRepository
import moscow.createdin.backend.repository.RentSlotToRentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.math.ceil

@Service
class RentService(
    private val properties: AkiProperties,
    private val userService: UserService,
    private val rentSlotService: RentSlotService,
    private val mailService: MailService,
    private val placeService: PlaceService,

    private val rentMapper: RentMapper,
    private val rentSlotMapper: RentSlotMapper,

    private val rentRepository: RentRepository,
    private val rentSlotToRentRepository: RentSlotToRentRepository,
    private val placeReviewRepository: PlaceReviewRepository
) {

    @Transactional
    fun create(req: CreateRentRequestDTO): RentDTO {
        val renter = userService.getCurrentUser()
        val place = placeService.findById(req.placeId)

        val agreementUrl: String = req.agreement.removePrefix("${properties.url}/${properties.imageUrlPrefix}/")
        val newRentId = rentRepository.create(req.placeId, renter.id, agreementUrl)
        rentSlotService.updateStatus(req.rentSlotIds, RentSlotStatus.BOOKED)
        rentSlotToRentRepository.create(newRentId, req.rentSlotIds)
        val rentSlots = mutableListOf<RentSlot>()
        for (slotId in req.rentSlotIds) {
            val rentSlot = rentSlotService.getById(slotId)
            rentSlots.add(rentSlot)
        }
        val minTimeStart: Instant = rentSlots.minOf { it.timeStart }
        val maxTimeEnd: Instant = rentSlots.maxOf { it.timeEnd }
        val agreement = "${properties.url}/${properties.imageUrlPrefix}/$agreementUrl"

        mailService.sendRentEmailToRenter(
            renter.email,
            minTimeStart,
            maxTimeEnd,
            "${renter.firstName} ${renter.lastName}",
            place.name,
            "${renter.firstName} ${renter.lastName}",
            agreement,
            properties.url,
            "${properties.url}/$LOGO_PATH"
        )
        val landlordEmail = place.user.email
        mailService.sendRentEmailToLandlord(
            landlordEmail,
            minTimeStart,
            maxTimeEnd,
            "${renter.firstName} ${renter.lastName}",
            place.name,
            "${place.user.firstName} ${place.user.lastName}", agreement, renter.email,
            properties.url,
            "${properties.url}/$LOGO_PATH"
        )

        return get(newRentId)
    }

    @Transactional
    fun getAll(
        pageNumber: Long,
        limit: Int
    ): RentListDTO {
        val renter = userService.getCurrentUser()

        val count = rentRepository.countByRenterId(renter.id)
        val total = ceil(count / limit.toDouble()).toInt()

        val rents = rentRepository.findByRenterId(pageNumber, limit, renter.id)
            .map { rent ->
                val slots = rentSlotToRentRepository.findSlotsByRentId(rent.id!!)
                    .map { rentSlotMapper.entityToDomain(it) }
                rentMapper.entityToDomain(rent, slots)
            }
            .map { rentMapper.domainToDto(it) }

        return RentListDTO(rents, total)
    }

    fun get(id: Long): RentDTO {
        return getDomain(id)
            .let { rentMapper.domainToDto(it) }
    }

    fun getDomain(id: Long): Rent {
        return rentRepository.findById(id)
            .let { rent ->
                val slots = rentSlotToRentRepository.findSlotsByRentId(rent.id!!)
                    .map { rentSlotMapper.entityToDomain(it) }
                rentMapper.entityToDomain(rent, slots)
            }
    }

    @Transactional
    fun cancelRent(req: RentCancelRequestDTO): RentDTO {
        val rent = rentRepository.findById(req.rentId)
        val canceledRent = rent.copy(
            status = RentConfirmationStatus.CANCELED.name,
            banReason = "Отменено пользователем"
        )
        rentRepository.update(canceledRent)
        freeRentSlots(req.rentId)

        return get(req.rentId)
    }

    @Transactional
    fun ban(req: BanRequestDTO): RentDTO {
        val adminUser = userService.getCurrentUserDomain()

        val rent = rentRepository.findById(req.id)
        val bannedRent = rent.copy(
            status = RentConfirmationStatus.BANNED.name,
            banReason = req.banReason,
            admin = adminUser.id
        )
        rentRepository.update(bannedRent)
        freeRentSlots(req.id)

        return get(req.id)
    }

    @Transactional
    fun rate(rentId: Long, rentReviewDTO: RentReviewDTO) {
        val user = userService.getCurrentUserDomain()
        rentRepository.findByRenterIdAndId(rentId, user.id)
        placeReviewRepository.save(rentId, rentReviewDTO.rating)
    }

    private fun freeRentSlots(rentId: Long) {
        val rentSlotIds = rentSlotToRentRepository.findSlotsByRentId(rentId)
            .map { it.id!! }
        rentSlotService.updateStatus(rentSlotIds, RentSlotStatus.OPEN)
    }

    companion object {
        private const val LOGO_PATH = "img/darkLogo.png"
    }
}
