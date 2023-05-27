package moscow.createdin.backend.service

import moscow.createdin.backend.getLogger
import moscow.createdin.backend.mapper.RentMapper
import moscow.createdin.backend.mapper.RentSlotMapper
import moscow.createdin.backend.model.dto.CreateRentRequestDTO
import moscow.createdin.backend.model.dto.RentDTO
import moscow.createdin.backend.model.enums.AdminStatusType
import moscow.createdin.backend.model.enums.RentSlotStatusType
import moscow.createdin.backend.repository.RentRepository
import moscow.createdin.backend.repository.RentSlotToRentRepository
import org.springframework.stereotype.Service

@Service
class RentService(
    private val userService: UserService,
    private val rentSlotService: RentSlotService,

    private val rentMapper: RentMapper,
    private val rentSlotMapper: RentSlotMapper,

    private val rentRepository: RentRepository,
    private val rentSlotToRentRepository: RentSlotToRentRepository
) {

    fun create(req: CreateRentRequestDTO): RentDTO {
        val renter = userService.getCurrentUser()

        val newRentId = rentRepository.create(req.placeId, renter.id)
        rentSlotService.updateStatus(req.rentSlotIds, RentSlotStatusType.BOOKED)
        rentSlotToRentRepository.create(newRentId, req.rentSlotIds)

        return get(newRentId)
    }

    fun getAll(): List<RentDTO> {
        val renter = userService.getCurrentUser()

        return rentRepository.findByRenterId(renter.id)
            .map { rent ->
                val slots = rentSlotToRentRepository.findSlotsByRentId(rent.id!!)
                    .map { rentSlotMapper.entityToDomain(it) }
                rentMapper.entityToDomain(rent, slots)
            }
            .map { rentMapper.domainToDto(it) }
    }

    fun get(id: Long): RentDTO {
        return rentRepository.findById(id)
            .let { rent ->
                val slots = rentSlotToRentRepository.findSlotsByRentId(rent.id!!)
                    .map { rentSlotMapper.entityToDomain(it) }
                rentMapper.entityToDomain(rent, slots)
            }
            .let { rentMapper.domainToDto(it) }
    }

    fun delete(id: Long) {
        val rent = rentRepository.findById(id)
        val deleteRent = rent.copy(status = AdminStatusType.DELETED.name)
        rentRepository.update(deleteRent)

        val rentSlotIds = rentSlotToRentRepository.findSlotsByRentId(id).map { it.id!! }
        rentSlotService.updateStatus(rentSlotIds, RentSlotStatusType.OPEN)
        rentSlotToRentRepository.delete(id)
    }

    companion object {
        private val log = getLogger<RentService>()
    }
}
