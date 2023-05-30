package moscow.createdin.backend.service

import moscow.createdin.backend.getLogger
import moscow.createdin.backend.mapper.RentMapper
import moscow.createdin.backend.mapper.RentSlotMapper
import moscow.createdin.backend.model.domain.Rent
import moscow.createdin.backend.model.dto.BanRequestDTO
import moscow.createdin.backend.model.dto.CreateRentRequestDTO
import moscow.createdin.backend.model.dto.RentDTO
import moscow.createdin.backend.model.dto.RentListDTO
import moscow.createdin.backend.model.enums.AdminStatusType
import moscow.createdin.backend.model.enums.RentConfirmationStatus
import moscow.createdin.backend.model.enums.RentSlotStatusType
import moscow.createdin.backend.repository.RentRepository
import moscow.createdin.backend.repository.RentSlotToRentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.ceil

@Service
class RentService(
    private val userService: UserService,
    private val rentSlotService: RentSlotService,
    private val mailService: MailService,
    private val placeService: PlaceService,

    private val rentMapper: RentMapper,
    private val rentSlotMapper: RentSlotMapper,

    private val rentRepository: RentRepository,
    private val rentSlotToRentRepository: RentSlotToRentRepository
) {

    @Transactional
    fun create(req: CreateRentRequestDTO): RentDTO {
        val renter = userService.getCurrentUser()

        val newRentId = rentRepository.create(req.placeId, renter.id, req.agreement)
        rentSlotService.updateStatus(req.rentSlotIds, RentSlotStatusType.BOOKED)
        rentSlotToRentRepository.create(newRentId, req.rentSlotIds)

        mailService.sendRentEmailToRenter(renter.email)
        val landlordEmail = placeService.findById(req.placeId).user.email
        mailService.sendRentEmailToLandlord(landlordEmail)

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
    fun delete(id: Long) {
        val rent = rentRepository.findById(id)
        val deleteRent = rent.copy(status = AdminStatusType.DELETED.name)
        rentRepository.update(deleteRent)

        val rentSlotIds = rentSlotToRentRepository.findSlotsByRentId(id).map { it.id!! }
        rentSlotService.updateStatus(rentSlotIds, RentSlotStatusType.OPEN)
        rentSlotToRentRepository.delete(id)
    }

    @Transactional
    fun ban(banRequestDTO: BanRequestDTO): RentDTO {
        val adminUser = userService.getCurrentUserDomain()
        val editable = getDomain(banRequestDTO.id)

        val result = editable.copy(
            status = RentConfirmationStatus.BANNED,
            banReason = banRequestDTO.banReason,
            admin = adminUser.id
        )

        return rentMapper.domainToEntity(result)
            .also { rentRepository.update(it) }
            .let { getDomain(it.id!!) }
            .let { rentMapper.domainToDto(it) }
    }

    companion object {
        private val log = getLogger<RentService>()
    }
}
