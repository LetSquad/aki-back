package moscow.createdin.backend.service

import moscow.createdin.backend.exception.ImageNotSavedException
import moscow.createdin.backend.exception.WrongUserException
import moscow.createdin.backend.mapper.AreaMapper
import moscow.createdin.backend.mapper.PlaceMapper
import moscow.createdin.backend.mapper.RentSlotMapper
import moscow.createdin.backend.model.domain.RentSlot
import moscow.createdin.backend.model.domain.place.Place
import moscow.createdin.backend.model.dto.BanRequestDTO
import moscow.createdin.backend.model.dto.place.NewPlaceDTO
import moscow.createdin.backend.model.dto.place.PlaceDTO
import moscow.createdin.backend.model.dto.place.PlaceListDTO
import moscow.createdin.backend.model.dto.place.UpdatePlaceDTO
import moscow.createdin.backend.model.entity.PlaceEntity
import moscow.createdin.backend.model.enums.PlaceConfirmationStatus
import moscow.createdin.backend.model.enums.PlaceSortDirection
import moscow.createdin.backend.model.enums.PlaceSortType
import moscow.createdin.backend.repository.PlaceRepository
import moscow.createdin.backend.repository.RentSlotRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class PlaceService(
    private val placeMapper: PlaceMapper,
    private val areaMapper: AreaMapper,
    private val placeRepository: PlaceRepository,
    private val placeImageService: PlaceImageService,
    private val areaService: AreaService,
    private val userService: UserService,
    private val rentSlotMapper: RentSlotMapper,
    private val rentSlotRepository: RentSlotRepository,
    private val filesystemService: FilesystemService
) {

    fun ban(banRequestDTO: BanRequestDTO): PlaceDTO {
        val adminUser = userService.getCurrentUserDomain()
        val editable = getDomain(banRequestDTO.bannedId)

        val result = editable.copy(
            status = PlaceConfirmationStatus.BANNED,
            banReason = banRequestDTO.reason,
            admin = adminUser.id
        )

        return placeMapper.domainToEntity(result)
            .also { placeRepository.update(it) }
            .let { placeRepository.findById(result.id!!) }
            .let {
                val rentSlots = getByPlaceId(it.id!!)
                placeMapper.entityToDomain(it, rentSlots)
            }
            .let { placeMapper.domainToDto(it, emptyList()) }
    }

    fun getPlaces(
        specialization: String?, capacity: Int?, fullAreaMin: Int?, fullAreaMax: Int?, levelNumberMin: Int?,
        levelNumberMax: Int?, parking: Boolean?, pageNumber: Long, limit: Int, sortType: PlaceSortType,
        sortDirection: PlaceSortDirection
    ): PlaceListDTO {
        val total = placeRepository.countByFilter(
            specialization, capacity, fullAreaMin, fullAreaMax,
            levelNumberMin, levelNumberMax, parking
        )

        val placeEntityList = placeRepository.findAll(
            specialization, capacity, fullAreaMin, fullAreaMax,
            levelNumberMin, levelNumberMax, parking, pageNumber, limit, getSortType(sortType), sortDirection
        )

        val placeImageMap = mutableMapOf<Long?, List<String>>()
        for (place in placeEntityList) {
            val placeImages: List<String> = placeImageService.getPlaceImages(place.id)
            placeImageMap[place.id] = placeImages
        }

        val places: List<PlaceDTO> = placeEntityList
            .map {
                val rentSlots = getByPlaceId(it.id!!)
                placeMapper.entityToDomain(it, rentSlots)
            }
            .map { placeMapper.domainToDto(it, placeImageMap[it.id]) }

        return PlaceListDTO(places, total)
    }

    @Transactional
    fun create(newPlaceDTO: NewPlaceDTO, images: Collection<MultipartFile>): PlaceDTO {
        val user = userService.getCurrentUserDomain()
        val area = newPlaceDTO.area?.let {
            areaService.get(newPlaceDTO.area)
                .let { areaMapper.dtoToDomain(it, user) }
                .let { areaMapper.domainToEntity(it) }
        }

        val place: Place = placeMapper.dtoToDomain(newPlaceDTO, user)
            .let { placeMapper.simpleDomainToEntity(it, user, area) }
            .let { placeRepository.save(it) }
            .let { placeRepository.findById(it) }
            .let {
                val rentSlots = getByPlaceId(it.id!!)
                placeMapper.entityToDomain(it, rentSlots)
            }

        val placeImages: List<String> = try {
            saveImages(place.id!!, images)
        } catch (e: Exception) {
            throw ImageNotSavedException(place.id, e)
        }

        return place.let { placeMapper.domainToDto(it, placeImages) }
    }

    @Transactional
    fun edit(updatePlace: UpdatePlaceDTO, images: Collection<MultipartFile>): PlaceDTO {
        val user = userService.getCurrentUserDomain()
        val oldPlace = findById(updatePlace.id)
        if (user.id != oldPlace.user.id) throw WrongUserException("wrong user with id = ${user.id} was id = ${oldPlace.user.id}")

        val placeImages: List<String> = if (images.isNotEmpty()) {
            placeImageService.clearPlaceImages(updatePlace.id)
            saveImages(updatePlace.id, images)
        } else {
            emptyList()
        }

        return placeMapper.updatedDtoToDomain(updatePlace, user, oldPlace.area?.id)
            .let { placeMapper.simpleDomainToEntity(it, user, oldPlace.area) }
            .let { placeRepository.update(it) }
            .let { placeRepository.findById(updatePlace.id) }
            .let {
                val rentSlots = getByPlaceId(it.id!!)
                placeMapper.entityToDomain(it, rentSlots)
            }
            .let { placeMapper.domainToDto(it, placeImages) }
    }

    fun findById(id: Long): PlaceEntity {
        return placeRepository.findById(id)
    }

    fun get(id: Long): PlaceDTO {
        val placeImages = placeImageService.getPlaceImages(id)
        return getDomain(id)
            .let { placeMapper.domainToDto(it, placeImages) }
    }

    fun getDomain(id: Long): Place {
        return placeRepository.findById(id)
            .let {
                val rentSlots = getByPlaceId(it.id!!)
                placeMapper.entityToDomain(it, rentSlots)
            }
    }

    fun getCurrentUserPlaces(
        pageNumber: Long,
        limit: Int
    ): PlaceListDTO {
        val user = userService.getCurrentUserDomain()
        val total = placeRepository.countByUserId(user.id!!)
        val places = placeRepository.findByUserId(pageNumber, limit, user.id)
            .map {
                val rentSlots = getByPlaceId(it.id!!)
                placeMapper.entityToDomain(it, rentSlots)
            }
            .map {
                val placeImages = placeImageService.getPlaceImages(it.id!!)
                placeMapper.domainToDto(it, placeImages)
            }

        return PlaceListDTO(places, total)
    }

    private fun getByPlaceId(placeId: Long): List<RentSlot> {
        return rentSlotRepository.findByPlaceId(placeId)
            .map { rentSlotMapper.entityToDomain(it) }
    }

    private fun saveImages(placeId: Long, images: Collection<MultipartFile>): List<String> = try {
        images.map { filesystemService.saveImage(it) to it.name.split("_")[1].toInt() }
            .onEach { (image, priority) -> placeImageService.savePlaceImage(placeId, image, priority) }
            .map { (image, _) -> image }
    } catch (e: Exception) {
        throw ImageNotSavedException(placeId, e)
    }

    private fun getSortType(sortType: PlaceSortType): String {
        return when (sortType) {
            PlaceSortType.personal -> "popular_count"   // TODO придумать как запросить по персональным рекомендациям
            PlaceSortType.popular -> "popular_count"
            PlaceSortType.rating -> "avg_rating"
            PlaceSortType.price -> "min_price"
        }
    }
}
