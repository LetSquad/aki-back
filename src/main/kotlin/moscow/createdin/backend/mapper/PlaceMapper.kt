package moscow.createdin.backend.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import moscow.createdin.backend.config.properties.AkiProperties
import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.domain.Price
import moscow.createdin.backend.model.domain.RentSlot
import moscow.createdin.backend.model.domain.place.Place
import moscow.createdin.backend.model.domain.place.PlaceEquipment
import moscow.createdin.backend.model.domain.place.PlaceFacility
import moscow.createdin.backend.model.domain.place.PlacePrice
import moscow.createdin.backend.model.domain.place.PlaceService
import moscow.createdin.backend.model.domain.place.SimplePlace
import moscow.createdin.backend.model.dto.RatingDTO
import moscow.createdin.backend.model.dto.place.NewPlaceDTO
import moscow.createdin.backend.model.dto.place.PlaceDTO
import moscow.createdin.backend.model.dto.place.PlaceEquipmentDTO
import moscow.createdin.backend.model.dto.place.PlaceFacilitiesDTO
import moscow.createdin.backend.model.dto.place.PlacePriceDTO
import moscow.createdin.backend.model.dto.place.PlaceServiceDTO
import moscow.createdin.backend.model.dto.place.UpdatePlaceDTO
import moscow.createdin.backend.model.entity.AreaEntity
import moscow.createdin.backend.model.entity.PlaceEntity
import moscow.createdin.backend.model.enums.PlaceConfirmationStatus
import moscow.createdin.backend.model.enums.PriceType
import org.postgresql.util.PGobject
import org.springframework.stereotype.Component


@Component
class PlaceMapper(
    private val akiProperties: AkiProperties,
    private val userMapper: UserMapper,
    private val areaMapper: AreaMapper,
    private val rentSlotMapper: RentSlotMapper,
    private val coordinatesMapper: CoordinatesMapper,
    private val gson: Gson
) {

    fun dtoToDomain(place: NewPlaceDTO, akiUser: AkiUser) = SimplePlace(
        id = null,
        user = akiUser,
        area = place.area,
        coordinates = place.coordinates,

        type = place.type,
        name = place.name,
        specialization = place.specialization,
        description = place.description,
        address = place.address,
        phone = place.phone,
        email = place.email,
        website = place.site,
        levelNumber = place.levelNumber,
        fullArea = place.fullSquare,
        rentableArea = place.freeSquare,
        minCapacity = place.minCapacity,
        maxCapacity = place.maxCapacity,
        parking = place.parking,

        services = place.services?.map { dtoToPlaceService(it) },
        rules = place.rules,
        accessibility = place.accessibility,
        facilities = place.facilities?.map { dtoToPlaceFacilities(it) },
        equipments = place.equipments?.map { dtoToPlaceEquipments(it) },

        status = PlaceConfirmationStatus.UNVERIFIED,
        banReason = null,
        admin = place.admin,
        price = null
    )

    fun updatedDtoToDomain(place: UpdatePlaceDTO, akiUser: AkiUser, area: Long?) = SimplePlace(
        id = place.id,
        user = akiUser,
        area = area,
        coordinates = null,

        type = "",
        name = place.name,
        specialization = place.specialization,
        description = place.description,
        address = "",
        phone = place.phone,
        email = place.email,
        website = place.site,
        levelNumber = -1,
        fullArea = -1,
        rentableArea = place.freeSquare,
        minCapacity = place.minCapacity,
        maxCapacity = place.maxCapacity,
        parking = place.parking,

        services = place.services?.map { dtoToPlaceService(it) },
        rules = place.rules,
        accessibility = place.accessibility,
        facilities = place.facilities?.map { dtoToPlaceFacilities(it) },
        equipments = place.equipments?.map { dtoToPlaceEquipments(it) },

        status = PlaceConfirmationStatus.UNVERIFIED,
        banReason = null,
        admin = place.admin,
        price = null
    )

    fun simpleDomainToEntity(
        place: SimplePlace, akiUser: AkiUser,
        area: AreaEntity?
    ) = PlaceEntity(
        id = place.id,
        user = userMapper.domainToEntity(akiUser),
        area = area,
        coordinates = area?.coordinates,

        type = place.type,
        name = place.name,
        specialization = place.specialization,
        description = place.description,
        address = place.address,
        phone = place.phone,
        email = place.email,
        website = place.website,
        levelNumber = place.levelNumber,
        fullArea = place.fullArea,
        rentableArea = place.rentableArea,
        minCapacity = place.minCapacity,
        maxCapacity = place.maxCapacity,
        parking = place.parking,

        services = stringToPGObject(gson.toJson(place.services)),
        rules = place.rules,
        accessibility = place.accessibility,
        facilities = stringToPGObject(gson.toJson(place.facilities)),
        equipments = stringToPGObject(gson.toJson(place.equipments)),

        status = place.status.name,
        banReason = place.banReason,
        admin = place.admin,
        minPrice = null,
        priceType = null,
        rating = null,
        rateCount = null,
        favorite = false
    )

    fun domainToEntity(place: Place) = PlaceEntity(
        id = place.id,
        user = userMapper.domainToEntity(place.user),
        area = place.area?.let { areaMapper.domainToEntity(place.area) },
        coordinates = place.coordinates?.let { coordinatesMapper.domainToEntity(place.coordinates) },

        type = place.type,
        name = place.name,
        specialization = place.specialization,
        description = place.description,
        address = place.address,
        phone = place.phone,
        email = place.email,
        website = place.website,
        levelNumber = place.levelNumber,
        fullArea = place.fullArea,
        rentableArea = place.rentableArea,
        minCapacity = place.minCapacity,
        maxCapacity = place.maxCapacity,
        parking = place.parking,

        services = stringToPGObject(gson.toJson(place.services)),
        rules = place.rules,
        accessibility = place.accessibility,
        facilities = stringToPGObject(gson.toJson(place.facilities)),
        equipments = stringToPGObject(gson.toJson(place.equipments)),

        status = place.status.name,
        banReason = place.banReason,
        admin = place.admin,
        minPrice = null,
        priceType = null,
        rating = null,
        rateCount = null,
        favorite = false
    )

    fun entityToDomain(place: PlaceEntity, rentSlots: List<RentSlot>?) = Place(
        id = place.id,
        user = userMapper.entityToDomain(place.user),
        area = place.area?.let { areaMapper.entityToDomain(place.area) },
        coordinates = place.coordinates?.let { coordinatesMapper.entityToDomain(place.coordinates) },

        type = place.type,
        name = place.name,
        specialization = place.specialization,
        description = place.description,
        address = place.address,
        phone = place.phone,
        email = place.email,
        website = place.website,
        levelNumber = place.levelNumber,
        fullArea = place.fullArea,
        rentableArea = place.rentableArea,
        minCapacity = place.minCapacity,
        maxCapacity = place.maxCapacity,
        parking = place.parking,

        services = gson.fromJson(
            place.services?.value ?: "[]",
            object : TypeToken<Collection<PlaceService?>?>() {}.type
        ),
        facilities = gson.fromJson(
            place.facilities?.value ?: "[]",
            object : TypeToken<Collection<PlaceFacility?>?>() {}.type
        ),
        equipments = gson.fromJson(
            place.equipments?.value ?: "[]",
            object : TypeToken<Collection<PlaceEquipment?>?>() {}.type
        ),

        status = PlaceConfirmationStatus.valueOf(place.status),
        banReason = place.banReason,
        admin = place.admin,
        price = place.priceType?.let { Price(place.minPrice, PriceType.valueOf(place.priceType)) },
        rules = place.rules,
        accessibility = place.accessibility,
        rentSlots = rentSlots,
        rating = place.rating,
        rateCount = place.rateCount,
        favorite = place.favorite
    )

    fun domainToDto(place: Place, placeImages: List<String>?) = PlaceDTO(
        id = place.id!!,
        user = userMapper.domainToPlaceDto(place.user),

        name = place.name,
        address = place.address,
        email = place.email,
        description = place.description,
        specialization = place.specialization,
        phone = place.phone,
        site = place.website,
        levelNumber = place.levelNumber,

        status = place.status,
        freeSquare = place.rentableArea,
        fullSquare = place.fullArea,
        minCapacity = place.minCapacity,
        maxCapacity = place.maxCapacity,
        parking = place.parking,
        price = PlacePriceDTO(place.price?.price, place.price?.priceType),
        services = place.services?.map { placeServiceToDTO(it) },
        placeImages = placeImages?.map { "${akiProperties.url}/${akiProperties.imageUrlPrefix}/${it}" },
        equipments = place.equipments?.map { placeEquipmentsToDTO(it) },
        facilities = place.facilities?.map { placeFacilitiesToDTO(it) },
        admin = place.admin,
        rentSlots = place.rentSlots?.map { rentSlotMapper.domainToDto(it) },
        rating = placeRatingToDTO(place.rating, place.rateCount),
        favorite = place.favorite
    )

    private fun stringToPGObject(value: String?): PGobject {
        val pGobject: PGobject = PGobject()
        pGobject.type = "jsonb"
        pGobject.value = value
        return pGobject
    }

    private fun placeServiceToDTO(placeService: PlaceService): PlaceServiceDTO {
        return PlaceServiceDTO(placeService.name, placePriceToDTO(placeService.price))
    }

    private fun placeEquipmentsToDTO(placeEquipment: PlaceEquipment): PlaceEquipmentDTO {
        return PlaceEquipmentDTO(placeEquipment.name, placePriceToDTO(placeEquipment.price), placeEquipment.count)
    }

    private fun placeFacilitiesToDTO(placeFacility: PlaceFacility): PlaceFacilitiesDTO {
        return PlaceFacilitiesDTO(placeFacility.name, placeFacility.count)
    }

    private fun placePriceToDTO(placePrice: PlacePrice): PlacePriceDTO {
        return PlacePriceDTO(placePrice.price, placePrice.priceType)
    }

    private fun placeRatingToDTO(rating: Double?, rateCount: Int?): RatingDTO {
        return RatingDTO(rating, rateCount)
    }

    private fun dtoToPlaceService(placeService: PlaceServiceDTO): PlaceService {
        return PlaceService(placeService.name, dtoToPlacePrice(placeService.price))
    }

    private fun dtoToPlaceEquipments(placeEquipment: PlaceEquipmentDTO): PlaceEquipment {
        return PlaceEquipment(placeEquipment.name, dtoToPlacePrice(placeEquipment.price), placeEquipment.count)
    }

    private fun dtoToPlaceFacilities(placeFacility: PlaceFacilitiesDTO): PlaceFacility {
        return PlaceFacility(placeFacility.name, placeFacility.count)
    }

    private fun dtoToPlacePrice(placePrice: PlacePriceDTO): PlacePrice {
        return PlacePrice(placePrice.price, placePrice.priceType)
    }
}
