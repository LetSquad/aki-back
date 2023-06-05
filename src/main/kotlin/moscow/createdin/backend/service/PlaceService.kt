package moscow.createdin.backend.service

import moscow.createdin.backend.config.properties.AkiProperties
import moscow.createdin.backend.exception.EmptyContextException
import moscow.createdin.backend.exception.ImageNotSavedException
import moscow.createdin.backend.exception.WrongUserException
import moscow.createdin.backend.getLogger
import moscow.createdin.backend.mapper.AreaMapper
import moscow.createdin.backend.mapper.PlaceMapper
import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.domain.place.Place
import moscow.createdin.backend.model.dto.BanRequestDTO
import moscow.createdin.backend.model.dto.CreateRentSlotRequestDTO
import moscow.createdin.backend.model.dto.place.NewPlaceDTO
import moscow.createdin.backend.model.dto.place.PlaceDTO
import moscow.createdin.backend.model.dto.place.PlaceListDTO
import moscow.createdin.backend.model.dto.place.UpdatePlaceDTO
import moscow.createdin.backend.model.entity.PlaceEntity
import moscow.createdin.backend.model.enums.PlaceConfirmationStatus
import moscow.createdin.backend.model.enums.PlaceSortDirection
import moscow.createdin.backend.model.enums.PlaceSortType
import moscow.createdin.backend.model.enums.SpecializationType
import moscow.createdin.backend.model.enums.UserRole
import moscow.createdin.backend.repository.FavoritePlaceRepository
import moscow.createdin.backend.repository.PlaceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.math.ceil

@Service
class PlaceService(
    private val properties: AkiProperties,
    private val placeMapper: PlaceMapper,
    private val areaMapper: AreaMapper,
    private val personalDigestService: PersonalPlaceDigestService,
    private val placeRepository: PlaceRepository,
    private val placeImageService: PlaceImageService,
    private val areaService: AreaService,
    private val userService: UserService,
    private val rentSlotService: RentSlotService,
    private val filesystemService: FilesystemService,
    private val favoritePlaceRepository: FavoritePlaceRepository
) {

    @Transactional
    fun approve(id: Long): PlaceDTO {
        val adminUser = userService.getCurrentUserDomain()
        val editable = getDomain(id)

        val result = editable.copy(
            status = PlaceConfirmationStatus.VERIFIED,
            admin = adminUser.id
        )

        return placeMapper.domainToEntity(result)
            .also { placeRepository.update(it) }
            .let { placeRepository.findById(result.id!!, adminUser.id) }
            .let {
                val rentSlots = rentSlotService.getByPlaceId(it.id!!)
                placeMapper.entityToDomain(it, rentSlots)
            }
            .let {
                val placeImages = placeImageService.getPlaceImages(it.id)
                placeMapper.domainToDto(it, placeImages)
            }
    }

    @Transactional
    fun ban(banRequestDTO: BanRequestDTO): PlaceDTO {
        val adminUser = userService.getCurrentUserDomain()
        val editable = getDomain(banRequestDTO.id)

        val result = editable.copy(
            status = PlaceConfirmationStatus.BANNED,
            banReason = banRequestDTO.banReason,
            admin = adminUser.id
        )

        return placeMapper.domainToEntity(result)
            .also { placeRepository.update(it) }
            .let { placeRepository.findById(result.id!!, adminUser.id) }
            .let {
                val rentSlots = rentSlotService.getByPlaceId(it.id!!)
                placeMapper.entityToDomain(it, rentSlots)
            }
            .let {
                val placeImages = placeImageService.getPlaceImages(it.id)
                placeMapper.domainToDto(it, placeImages)
            }
    }

    @Transactional
    fun getPlaces(
        specialization: List<SpecializationType>?,
        rating: Int?,
        priceMin: Int?,
        priceMax: Int?,
        capacityMin: Int?,
        capacityMax: Int?,
        squareMin: Int?,
        squareMax: Int?,
        levelNumberMin: Int?,
        levelNumberMax: Int?,
        withParking: Boolean?,
        dateFrom: LocalDate?,
        dateTo: LocalDate?,
        metroStations: List<String>?,

        pageNumber: Long,
        limit: Int,
        sortType: PlaceSortType,
        sortDirection: PlaceSortDirection
    ): PlaceListDTO {
        val user: AkiUser? = try {
            userService.getCurrentUserDomain()
        } catch (e: EmptyContextException) {
            null
        }

        val count = placeRepository.countByFilter(
            specialization ?: emptyList(),
            rating,
            priceMin,
            priceMax,
            capacityMin,
            capacityMax,
            squareMin,
            squareMax,
            levelNumberMin,
            levelNumberMax,
            withParking,
            dateFrom?.let { Timestamp.from(dateFrom.atStartOfDay().toInstant(ZoneOffset.UTC)) },
            dateTo?.let { Timestamp.from(dateTo.atStartOfDay().toInstant(ZoneOffset.UTC)) },
            user?.id,
            metroStations
        )

        val total = ceil(count / limit.toDouble()).toInt()

        val placeList = if (sortType == PlaceSortType.personal) {
            personalDigestService.getPersonalPlaces(
                specialization,
                rating,
                priceMin,
                priceMax,
                capacityMin,
                capacityMax,
                squareMin,
                squareMax,
                levelNumberMin,
                levelNumberMax,
                withParking,
                dateFrom,
                dateTo,
                metroStations,
                pageNumber,
                limit,
                sortDirection,
                user
            )
        } else {
            placeRepository.findAll(
                specialization ?: emptyList(),
                rating,
                priceMin,
                priceMax,
                capacityMin,
                capacityMax,
                squareMin,
                squareMax,
                levelNumberMin,
                levelNumberMax,
                withParking,
                dateFrom?.let { Timestamp.from(dateFrom.atStartOfDay().toInstant(ZoneOffset.UTC)) },
                dateTo?.let { Timestamp.from(dateTo.atStartOfDay().toInstant(ZoneOffset.UTC)) },
                metroStations,

                pageNumber,
                limit,
                user?.id,
                sortType,
                sortDirection
            ).map {
                val rentSlots = rentSlotService.getByPlaceId(it.id!!)
                placeMapper.entityToDomain(it, rentSlots)
            }
        }

        val placeImageMap = mutableMapOf<Long?, List<String>>()
        for (place in placeList) {
            val placeImages: List<String> = placeImageService.getPlaceImages(place.id)
            placeImageMap[place.id] = placeImages
        }

        val places: List<PlaceDTO> = placeList
            .map { placeMapper.domainToDto(it, placeImageMap[it.id]) }

        return PlaceListDTO(places, total)
    }

    fun delete(id: Long) {
        val editableArea = getDomain(id)

        val result = editableArea.copy(status = PlaceConfirmationStatus.DELETED)

        placeMapper.domainToEntity(result)
            .also { placeRepository.update(it) }
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
            .let { placeRepository.findById(it, user.id) }
            .let {
                val rentSlots = rentSlotService.getByPlaceId(it.id!!)
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

        placeImageService.clearPlaceImages(
            placeId = updatePlace.id,
            images = updatePlace.placeImages
                ?.map { it.removePrefix("${properties.url}/${properties.dataPath}/") }
        )
        saveImages(updatePlace.id, images)

        return placeMapper.updatedDtoToDomain(updatePlace, user, oldPlace.area?.id)
            .let { placeMapper.simpleDomainToEntity(it, user, oldPlace.area) }
            .let { placeRepository.update(it) }
            .let { placeRepository.findById(updatePlace.id, user.id) }
            .let {
                val rentSlots = rentSlotService.getByPlaceId(it.id!!)
                placeMapper.entityToDomain(it, rentSlots)
            }
            .let { placeMapper.domainToDto(it, placeImageService.getPlaceImages(it.id)) }
    }

    fun findById(id: Long): PlaceEntity {
        val user = userService.getCurrentUserDomain()
        return placeRepository.findById(id, user.id)
    }

    fun get(id: Long): PlaceDTO {
        val placeImages = placeImageService.getPlaceImages(id)
        return getDomain(id)
            .let { placeMapper.domainToDto(it, placeImages) }
    }

    fun getDomain(id: Long): Place {
        val user = userService.getCurrentUserDomain()
        return placeRepository.findById(id, user.id)
            .let {
                val rentSlots = rentSlotService.getByPlaceId(it.id!!)
                placeMapper.entityToDomain(it, rentSlots)
            }
    }

    @Transactional
    fun getCurrentUserPlaces(
        pageNumber: Long,
        limit: Int
    ): PlaceListDTO {
        val user = userService.getCurrentUserDomain()

        when (user.role) {
            UserRole.LANDLORD -> {
                val count = placeRepository.countByUserId(user.id!!)
                val total = ceil(count / limit.toDouble()).toInt()

            val places = placeRepository.findByUserId(pageNumber, limit, user.id)
                .map {
                    val rentSlots = rentSlotService.getByPlaceId(it.id!!)
                    placeMapper.entityToDomain(it, rentSlots)
                }
                .map {
                    val placeImages = placeImageService.getPlaceImages(it.id!!)
                    placeMapper.domainToDto(it, placeImages)
                }

                return PlaceListDTO(places, total)
            }

            UserRole.ADMIN -> {
                val count = placeRepository.countUnverified()
                val total = ceil(count / limit.toDouble()).toInt()

            val places = placeRepository.findUnverified(pageNumber, limit)
                .map {
                    val rentSlots = rentSlotService.getByPlaceId(it.id!!)
                    placeMapper.entityToDomain(it, rentSlots)
                }
                .map {
                    val placeImages = placeImageService.getPlaceImages(it.id!!)
                    placeMapper.domainToDto(it, placeImages)
                }

                return PlaceListDTO(places, total)
            }

            UserRole.RENTER -> {
                val count = placeRepository.countFavorite(user.id)
                val total = ceil(count / limit.toDouble()).toInt()

                val places = placeRepository.findFavorite(pageNumber, limit, user.id)
                    .map {
                        val rentSlots = rentSlotService.getByPlaceId(it.id!!)
                        placeMapper.entityToDomain(it, rentSlots)
                    }
                    .map {
                        val placeImages = placeImageService.getPlaceImages(it.id!!)
                        placeMapper.domainToDto(it, placeImages)
                    }

                return PlaceListDTO(places, total)
            }
        }
    }

    fun createFavorite(placeId: Long): PlaceDTO {
        val user = userService.getCurrentUserDomain()
        favoritePlaceRepository.save(placeId, user.id)
        return get(id = placeId)
    }

    fun deleteFavorite(placeId: Long): PlaceDTO {
        val user = userService.getCurrentUserDomain()
        favoritePlaceRepository.delete(placeId, user.id)
        return get(id = placeId)
    }

    fun createRentSlots(list: List<CreateRentSlotRequestDTO>): PlaceDTO {
        rentSlotService.create(list)

        return findById(list.first().placeId)
            .let {
                val rentSlots = rentSlotService.getByPlaceId(it.id!!)
                placeMapper.entityToDomain(it, rentSlots)
            }
            .let { placeMapper.domainToDto(it, listOf()) }
    }

    fun deleteRentSlots(ids: List<Long>): PlaceDTO {
        rentSlotService.delete(ids)

        val placeId = rentSlotService.getById(ids.first()).placeId!!
        return findById(placeId)
            .let {
                val rentSlots = rentSlotService.getByPlaceId(it.id!!)
                placeMapper.entityToDomain(it, rentSlots)
            }
            .let { placeMapper.domainToDto(it, listOf()) }
    }

    private fun saveImages(placeId: Long, images: Collection<MultipartFile>): List<String> = try {
        images.map { filesystemService.saveImage(it) to it.name.split("_")[1].toInt() }
            .onEach { (image, priority) -> placeImageService.savePlaceImage(placeId, image, priority) }
            .map { (image, _) -> image }
    } catch (e: Exception) {
        throw ImageNotSavedException(placeId, e)
    }

    companion object {
        private val log = getLogger<UserService>()
    }
}
