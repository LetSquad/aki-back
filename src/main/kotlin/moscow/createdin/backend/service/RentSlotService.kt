package moscow.createdin.backend.service

import moscow.createdin.backend.exception.UnprocessableCreateRentSlotRequestException
import moscow.createdin.backend.getLogger
import moscow.createdin.backend.mapper.RentSlotMapper
import moscow.createdin.backend.model.domain.RentSlot
import moscow.createdin.backend.model.domain.RentSlotTime
import moscow.createdin.backend.model.dto.CreateRentSlotRequestDTO
import moscow.createdin.backend.model.enums.RentSlotStatusType
import moscow.createdin.backend.repository.RentSlotRepository
import org.springframework.stereotype.Service

@Service
class RentSlotService(
    private val rentSlotMapper: RentSlotMapper,
    private val rentSlotRepository: RentSlotRepository
) {

    fun create(list: List<CreateRentSlotRequestDTO>) {
        validate(list)

        list.map { rentSlotMapper.createDtoToDomain(it) }
            .map { rentSlotMapper.domainToEntity(it) }
            .also { rentSlotRepository.save(it) }
    }

    fun validate(list: List<CreateRentSlotRequestDTO>) {
        if (list.isEmpty()) {
            throw UnprocessableCreateRentSlotRequestException("empty request")
        }

        val allSlots = getByPlaceId(list[0].placeId)
            .map { RentSlotTime(it.timeStart, it.timeEnd) }.toMutableList()
        allSlots.addAll(
            list.map { RentSlotTime(it.timeStart.toInstant(), it.timeEnd.toInstant()) }
        )

        if (allSlots.size < 2) {
            return
        }

        for (i in 0 until allSlots.size) {
            for (j in 0 until allSlots.size){
                if (j != i
                    && (allSlots[i].timeStart.isAfter(allSlots[j].timeStart) && allSlots[i].timeStart.isBefore(allSlots[j].timeEnd)
                            || allSlots[i].timeEnd.isAfter(allSlots[j].timeStart) && allSlots[i].timeEnd.isBefore(allSlots[j].timeEnd))) {
                    throw UnprocessableCreateRentSlotRequestException("time crossing in request")
                }
            }
        }
    }

    fun getById(id: Long): RentSlot {
        return rentSlotRepository.findById(id)
            .let { rentSlotMapper.entityToDomain(it) }
    }

    fun getByPlaceId(placeId: Long): List<RentSlot> {
        return rentSlotRepository.findByPlaceId(placeId)
            .map { rentSlotMapper.entityToDomain(it) }
    }

    fun delete(ids: List<Long>) {
        updateStatus(ids, RentSlotStatusType.DELETED)
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

    fun getByPlaceIdFutureSlots(placeId: Long): List<RentSlot> {
        return rentSlotRepository.findByPlaceIdFuture(placeId)
            .map { rentSlotMapper.entityToDomain(it) }
    }

    companion object {
        private val log = getLogger<RentSlotService>()
    }
}
