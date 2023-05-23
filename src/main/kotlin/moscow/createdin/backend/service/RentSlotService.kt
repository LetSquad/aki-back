package moscow.createdin.backend.service

import moscow.createdin.backend.getLogger
import moscow.createdin.backend.mapper.PlaceMapper
import moscow.createdin.backend.mapper.RentSlotMapper
import moscow.createdin.backend.model.domain.RentSlot
import moscow.createdin.backend.model.dto.CreateRentSlotRequestDTO
import moscow.createdin.backend.model.dto.place.PlaceDTO
import moscow.createdin.backend.model.dto.RentSlotDTO
import moscow.createdin.backend.model.enums.RentSlotStatusType
import moscow.createdin.backend.repository.RentSlotRepository
import org.springframework.stereotype.Service

@Service
class RentSlotService(
    private val placeService: PlaceService,
    private val placeMapper: PlaceMapper,
    private val rentSlotMapper: RentSlotMapper,
    private val rentSlotRepository: RentSlotRepository
) {

    fun create(list: List<CreateRentSlotRequestDTO>): PlaceDTO {
        list.map { rentSlotMapper.createDtoToDomain(it) }
            .map { rentSlotMapper.domainToEntity(it) }
            .also { rentSlotRepository.save(it) }

        return placeService.findById(list[0].placeId)
            .let { placeMapper.entityToDomain(it) }
            .let { placeMapper.domainToDto(it, listOf()) }
    }

    fun getById(id: Long): RentSlot {
        return rentSlotRepository.findById(id)
            .let { rentSlotMapper.entityToDomain(it) }
    }

    fun getByPlaceId(placeId: Long): List<RentSlotDTO> {
        return rentSlotRepository.findByPlaceId(placeId)
            .map { rentSlotMapper.entityToDomain(it) }
            .map { rentSlotMapper.domainToDto(it) }
    }

    fun delete(ids: List<Long>): PlaceDTO {
        updateStatus(ids, RentSlotStatusType.DELETED)

        val placeId = getById(ids[0]).placeId!!
        return placeService.findById(placeId)
            .let { placeMapper.entityToDomain(it) }
            .let { placeMapper.domainToDto(it, listOf()) }
    }

    fun updateStatus(
        ids: List<Long>,
        statusType: RentSlotStatusType
    ) {
        ids
            .map { getById(it) }
            .map { it.copy(status = statusType) }
            .map { rentSlotMapper.domainToEntity(it) }
            .let { rentSlotRepository.update(it) }
    }

    companion object {
        private val log = getLogger<RentSlotService>()
    }
}
