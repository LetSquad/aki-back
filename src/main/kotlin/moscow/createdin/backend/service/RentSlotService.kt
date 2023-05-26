package moscow.createdin.backend.service

import moscow.createdin.backend.getLogger
import moscow.createdin.backend.mapper.RentSlotMapper
import moscow.createdin.backend.model.domain.RentSlot
import moscow.createdin.backend.model.dto.CreateRentSlotRequestDTO
import moscow.createdin.backend.model.dto.PlaceDTO
import moscow.createdin.backend.model.dto.RentSlotDTO
import moscow.createdin.backend.model.enums.RentSlotStatusType
import moscow.createdin.backend.repository.RentSlotRepository
import org.springframework.stereotype.Service

@Service
class RentSlotService(
    private val rentSlotMapper: RentSlotMapper,
    private val rentSlotRepository: RentSlotRepository
) {

    fun create(list: List<CreateRentSlotRequestDTO>): PlaceDTO {
        list.map { rentSlotMapper.createDtoToDomain(it) }
            .map { rentSlotMapper.domainToEntity(it) }
            .also { rentSlotRepository.save(it) }

        // TODO добавить нормальное создание модели площадки
        return PlaceDTO(id = 0)
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
        ids
            .map { getById(it) }
            .map { it.copy(status = RentSlotStatusType.DELETED) }
            .map { rentSlotMapper.domainToEntity(it) }
            .let { rentSlotRepository.update(it) }

        // TODO добавить нормальное создание модели площадки
        return PlaceDTO(id = 0)
    }

    companion object {
        private val log = getLogger<RentSlotService>()
    }
}
