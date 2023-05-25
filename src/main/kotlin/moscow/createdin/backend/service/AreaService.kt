package moscow.createdin.backend.service

import moscow.createdin.backend.getLogger
import moscow.createdin.backend.mapper.AreaMapper
import moscow.createdin.backend.model.domain.Area
import moscow.createdin.backend.model.dto.AreaDTO
import moscow.createdin.backend.model.dto.AreaDTOList
import moscow.createdin.backend.model.dto.BanRequestDTO
import moscow.createdin.backend.model.dto.CreateAreaRequestDTO
import moscow.createdin.backend.model.dto.EditAreaRequestDTO
import moscow.createdin.backend.model.enums.AdminStatusType
import moscow.createdin.backend.repository.AreaRepository
import org.springframework.stereotype.Service

@Service
class AreaService(
    private val userService: UserService,
    private val areaMapper: AreaMapper,
    private val areaRepository: AreaRepository
) {

    fun create(req: CreateAreaRequestDTO): AreaDTO {
        val user = userService.getCurrentUserDomain()
        val area: Area = areaMapper.createDtoToDomain(req, user)
        return areaMapper.domainToEntity(area)
            .let { areaRepository.save(it) }
            .let { areaRepository.findById(it) }
            .let { areaMapper.entityToDomain(it) }
            .let { areaMapper.domainToDto(it) }
    }

    fun get(id: Long): AreaDTO {
        return areaRepository.findById(id)
            .let { areaMapper.entityToDomain(it) }
            .let { areaMapper.domainToDto(it) }
    }

    fun get(): AreaDTOList {
        val currentUser = userService.getCurrentUser()
        return getByUser(currentUser.id)
    }

    fun edit(req: EditAreaRequestDTO) {
        val currentUser = userService.getCurrentUserDomain()
        val editableArea = areaRepository.findById(req.id)
            .let { areaMapper.entityToDomain(it) }

        if (editableArea.user.id?.equals(currentUser.id) == false) {
            log.error("Area not provide to current landlord")
            return
        }

        val result = editableArea.copy(
            status = AdminStatusType.UNVERIFIED,
            name = req.name,
            description = req.description,
            areaImage = req.areaImage ?: editableArea.areaImage,
            address = req.address,
            website = req.website ?: editableArea.website,
            email = req.email ?: editableArea.email,
            phone = req.phone ?: editableArea.phone,
//            coordinates = req.coordinates?.let { coordinatesMapper.dtoToDomain(req.coordinates) } // TODO
        )

        areaMapper.domainToEntity(result)
            .also { areaRepository.update(it) }
    }

    fun getByUser(userId: Long): AreaDTOList {
        val list = areaRepository.findByUserId(userId)
            .map { areaMapper.entityToDomain(it) }
            .map { areaMapper.domainToDto(it) }

        return AreaDTOList(list, list.size)
    }

    fun verify(areaId: Long) {
        val adminUser = userService.getCurrentUserDomain()
        val editable = areaRepository.findById(areaId)
            .let { areaMapper.entityToDomain(it) }

        val result = editable.copy(
            status = AdminStatusType.VERIFIED,
            admin = adminUser
        )

        areaMapper.domainToEntity(result)
            .also { areaRepository.update(it) }
    }

    fun ban(banRequestDTO: BanRequestDTO) {
        val adminUser = userService.getCurrentUserDomain()
        val editable = areaRepository.findById(banRequestDTO.bannedId)
            .let { areaMapper.entityToDomain(it) }

        val result = editable.copy(
            status = AdminStatusType.BANNED,
            banReason = banRequestDTO.reason,
            admin = adminUser
        )

        areaMapper.domainToEntity(result)
            .also { areaRepository.update(it) }
    }

    fun delete(areaId: Long) {
        val currentUser = userService.getCurrentUserDomain()
        val editableArea = areaRepository.findById(areaId)
            .let { areaMapper.entityToDomain(it) }

        if (editableArea.user.id?.equals(currentUser.id) == false) {
            log.error("Area not provide to current landlord")
            return
        }

        val result = editableArea.copy(status = AdminStatusType.DELETED)

        areaMapper.domainToEntity(result)
            .also { areaRepository.update(it) }
    }

    companion object {
        private val log = getLogger<AreaService>()
    }
}
