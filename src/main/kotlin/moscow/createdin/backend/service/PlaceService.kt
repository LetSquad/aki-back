package moscow.createdin.backend.service

import moscow.createdin.backend.exception.WrongUserExceptionException
import moscow.createdin.backend.mapper.AreaMapper
import moscow.createdin.backend.mapper.PlaceMapper
import moscow.createdin.backend.model.dto.place.NewPlaceDTO
import moscow.createdin.backend.model.dto.place.PlaceDTO
import moscow.createdin.backend.model.dto.place.PlaceListDTO
import moscow.createdin.backend.model.dto.place.UpdatePlaceDTO
import moscow.createdin.backend.model.entity.PlaceEntity
import moscow.createdin.backend.model.enums.PlaceSortDirection
import moscow.createdin.backend.model.enums.PlaceSortType
import moscow.createdin.backend.repository.PlaceRepository
import org.springframework.stereotype.Service

@Service
class PlaceService(
    private val placeMapper: PlaceMapper,
    private val areaMapper: AreaMapper,
    private val placeRepository: PlaceRepository,
    private val placeImageService: PlaceImageService,
    private val areaService: AreaService,
    private val userService: UserService,
    private val filesystemService: FilesystemService
) {

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
            .map { placeMapper.entityToDomain(it) }
            .map { placeMapper.domainToDto(it, placeImageMap[it.id]) }

        return PlaceListDTO(places, total)
    }

    fun create(newPlaceDTO: NewPlaceDTO): PlaceDTO {
        val user = userService.getCurrentUserDomain()
        val area = newPlaceDTO.area?.let {
            areaService.get(newPlaceDTO.area)
                .let { areaMapper.dtoToDomain(it, user) }
                .let { areaMapper.domainToEntity(it) }
        }


//        if (images.isNotEmpty()) {
//            try {
//                val placeImages = mutableListOf<String>()
//                for (image in images) {
//                    placeImages.add(filesystemService.saveImage(image))
//                }
//            } catch (e: Exception) {
//                throw ImageNotSavedException(place.id, e)
//            }
//        }
//        val placeImages = placeImageService.getPlaceImages(place.id)

        //TODO add placeImages...
        val placeImages = emptyList<String>()

        return placeMapper.dtoToDomain(newPlaceDTO, user)
            .let { placeMapper.simpleDomainToEntity(it, user, area) }
            .let { placeRepository.save(it) }
            .let { placeRepository.findById(it) }
            .let { placeMapper.entityToDomain(it) }
            .let { placeMapper.domainToDto(it, placeImages) }
    }

    fun edit(updatePlaceDTO: UpdatePlaceDTO): PlaceDTO {
        val user = userService.getCurrentUserDomain()
        val oldPlace = findById(updatePlaceDTO.id)
        if (user.id != oldPlace.user.id) throw WrongUserExceptionException("wrong user with id = ${user.id} was id = ${oldPlace.user.id}")

        return placeMapper.updatedDtoToDomain(updatePlaceDTO, user, oldPlace.area?.id)
            .let { placeMapper.simpleDomainToEntity(it, user, oldPlace.area) }
            .let { placeRepository.update(it) }
            .let { placeRepository.findById(updatePlaceDTO.id) }
            .let { placeMapper.entityToDomain(it) }
            .let { placeMapper.domainToDto(it, emptyList()) }
    }

    fun findById(id: Long): PlaceEntity {
        return placeRepository.findById(id)
    }

    fun get(id: Long): PlaceDTO {
        val placeImages = getImages(id)
        return placeRepository.findById(id)
            .let { placeMapper.entityToDomain(it) }
            .let { placeMapper.domainToDto(it, placeImages) }
    }

    fun getCurrentUserPlaces(): PlaceListDTO {
        val user = userService.getCurrentUserDomain()
        val places = placeRepository.findByUserId(user.id!!)
            .map { placeMapper.entityToDomain(it) }
            .map {
                val placeImages = getImages(it.id!!)
                placeMapper.domainToDto(it, placeImages) }

        return PlaceListDTO(places, places.size)
    }

    // todo добавить получение картинок
    fun getImages(placeId: Long): List<String>? {
        return null
    }

    private fun getSortType(sortType: PlaceSortType): String {
        return when (sortType) {
            PlaceSortType.PERSONAL -> "personal"
            PlaceSortType.POPULAR -> "popular_count"
            PlaceSortType.RATING -> "avg_rating"
            PlaceSortType.PRICE -> "min_price"
        }
    }
}
