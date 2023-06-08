package moscow.createdin.backend.service

import moscow.createdin.backend.mapper.PlaceMapper
import moscow.createdin.backend.model.domain.AkiUser
import moscow.createdin.backend.model.domain.place.Place
import moscow.createdin.backend.model.entity.PlaceEntity
import moscow.createdin.backend.model.enums.PlaceSortDirection
import moscow.createdin.backend.model.enums.SpecializationType
import moscow.createdin.backend.model.enums.UserSpecialization
import moscow.createdin.backend.repository.PlaceRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.stream.Collectors
import kotlin.math.roundToInt

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

        val placeSpecializations: Set<SpecializationType> = user?.specializations
            ?.map { getPlaceSpecializations(it) }
            ?.fold(emptySet()) { acc, placeSpecs -> acc + placeSpecs }
            ?: emptySet()
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

        val ratingWeight: Int = place.rating?.roundToInt() ?: 0

        return favoriteWeight + specializationWeight + ratingWeight
    }

    private fun getPlaceSpecializations(userSpec: UserSpecialization): Set<SpecializationType> = when (userSpec) {
        UserSpecialization.ART -> setOf(
            SpecializationType.CREATIVE_SPACE,
            SpecializationType.GALLERY,
            SpecializationType.SHOWROOM,
            SpecializationType.ART_WORKSHOP,
            SpecializationType.SHOW_SPACE
        )
        UserSpecialization.ARCHITECTURE -> setOf(
            SpecializationType.PROTOTYPING_CENTER,
            SpecializationType.LAYOUT_WORKSHOP,
            SpecializationType.RENDERING_STUDIO
        )
        UserSpecialization.VIDEO_GAMES_AND_SOFTWARE -> setOf(
            SpecializationType.AR_VR_STUDIOS,
            SpecializationType.SOUND_RECORDING_STUDIO,
            SpecializationType.RENDERING_STUDIO,
            SpecializationType.MOKAP_STUDIO
        )
        UserSpecialization.DESIGN -> setOf(
            SpecializationType.DESIGN_STUDIO,
            SpecializationType.RENDERING_STUDIO
        )
        UserSpecialization.PUBLISHING_AND_NEW_MEDIA -> setOf(
            SpecializationType.BOOKSTORE,
            SpecializationType.FILM_STUDIO,
            SpecializationType.PUBLISHING_HOUSE,
            SpecializationType.RENDERING_STUDIO,
            SpecializationType.PHOTO_VIDEO_STUDIO
        )
        UserSpecialization.PERFORMING_ARTS -> setOf(
            SpecializationType.CREATIVE_SPACE,
            SpecializationType.DANCE_HALL,
            SpecializationType.REHEARSAL_ROOM,
            SpecializationType.MOKAP_STUDIO
        )
        UserSpecialization.FILM_AND_ANIMATION -> setOf(
            SpecializationType.CINEMA,
            SpecializationType.FILM_STUDIO,
            SpecializationType.VIDEO_EDITING_STUDIO,
            SpecializationType.MOKAP_STUDIO
        )
        UserSpecialization.FASHION -> setOf(
            SpecializationType.SEWING_SHOP,
            SpecializationType.SHOWROOM
        )
        UserSpecialization.MUSIC -> setOf(
            SpecializationType.SOUND_RECORDING_STUDIO,
            SpecializationType.REHEARSAL_ROOM,
            SpecializationType.DANCE_HALL,
            SpecializationType.MUSIC_REHEARSAL_STUDIO,
            SpecializationType.CONCERT_HALL
        )
        UserSpecialization.ADVERTISING -> setOf(
            SpecializationType.CREATIVE_SPACE,
            SpecializationType.PUBLISHING_HOUSE,
            SpecializationType.SOUND_RECORDING_STUDIO,
            SpecializationType.FILM_STUDIO,
            SpecializationType.VIDEO_EDITING_STUDIO,
        )
    }
}
