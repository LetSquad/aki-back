package moscow.createdin.backend.service

import moscow.createdin.backend.mapper.PlaceMapper
import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.domain.place.Place
import moscow.createdin.backend.model.entity.PlaceEntity
import moscow.createdin.backend.model.enums.PlaceSortDirection
import moscow.createdin.backend.model.enums.SpecializationType
import moscow.createdin.backend.model.enums.UserType
import moscow.createdin.backend.repository.PlaceRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.stream.Collectors

@Service
class PersonalPlaceDigestService(
    private val placeMapper: PlaceMapper,
    private val rentSlotService: RentSlotService,
    private val placeRepository: PlaceRepository
) {

    fun getPersonalPlaces(
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
        sortDirection: PlaceSortDirection,

        user: AkiUser?
    ): List<Place> {
        val placeEntityList = placeRepository.findAll(
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
            user?.id
        )

        val placeSpecializations: Set<SpecializationType> = getSpecializationsForUser(user?.type)
        val sortedPlaces = when (sortDirection) {
            PlaceSortDirection.ASC -> placeEntityList.sortedBy {
                calculateWeightForPlace(it, placeSpecializations)
            }
            PlaceSortDirection.DESC -> placeEntityList.sortedByDescending {
                calculateWeightForPlace(it, placeSpecializations)
            }
        }

        return sortedPlaces.stream()
            .skip((pageNumber - 1) * limit)
            .limit(limit.toLong())
            .map {
                val rentSlots = rentSlotService.getByPlaceId(it.id!!)
                placeMapper.entityToDomain(it, rentSlots)
            }
            .collect(Collectors.toList())
    }

    private fun calculateWeightForPlace(place: PlaceEntity, specializations: Set<SpecializationType>): Int {
        val favoriteWeight: Int = if (place.favorite) 40 else 0

        val specializationWeight: Int = if (place.specialization.any { specializations.contains(it) }) {
            10
        } else {
            0
        }

        return favoriteWeight + specializationWeight
    }

    private fun getSpecializationsForUser(userType: UserType?): Set<SpecializationType> = when (userType) {
        UserType.MUSICIAN -> setOf(
            SpecializationType.SOUND_RECORDING_STUDIO,
            SpecializationType.REHEARSAL_ROOM,
            SpecializationType.DANCE_HALL,
            SpecializationType.MUSIC_REHEARSAL_STUDIO,
            SpecializationType.CONCERT_HALL
        )
        UserType.SINGER -> setOf(
            SpecializationType.SOUND_RECORDING_STUDIO,
            SpecializationType.REHEARSAL_ROOM,
            SpecializationType.STAGE_SPACE,
            SpecializationType.MUSIC_REHEARSAL_STUDIO,
            SpecializationType.CONCERT_HALL
        )
        UserType.BUSINESSMAN -> setOf(
            SpecializationType.AR_VR_STUDIOS,
            SpecializationType.CREATIVE_SPACE,
            SpecializationType.GALLERY,
            SpecializationType.PUBLISHING_HOUSE,
            SpecializationType.SHOW_SPACE
        )
        UserType.TEACHER -> setOf(
            SpecializationType.BOOKSTORE,
            SpecializationType.ART_WORKSHOP,
            SpecializationType.PROTOTYPING_CENTER,
            SpecializationType.LAYOUT_WORKSHOP,
            SpecializationType.RENDERING_STUDIO,
            SpecializationType.REHEARSAL_ROOM,
            SpecializationType.SEWING_SHOP
        )
        null -> emptySet()
    }
}
