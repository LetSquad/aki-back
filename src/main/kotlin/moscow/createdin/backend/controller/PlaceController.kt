package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Parameter
import moscow.createdin.backend.model.dto.place.NewPlaceDTO
import moscow.createdin.backend.model.dto.place.PlaceDTO
import moscow.createdin.backend.model.dto.place.PlaceListDTO
import moscow.createdin.backend.model.dto.place.UpdatePlaceDTO
import moscow.createdin.backend.model.enums.PlaceSortDirection
import moscow.createdin.backend.model.enums.PlaceSortType
import moscow.createdin.backend.service.PlaceService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/places")
class PlaceController(private val placeService: PlaceService) {

    @GetMapping
    fun getPlaces(
        @Parameter(description = "Специализация площадки") @RequestParam specialization: String?,
        @Parameter(description = "Желаемая вместимость площадки") @RequestParam capacity: Int?,
        @Parameter(description = "Площадь площадки минимальная") @RequestParam fullAreaMin: Int?,
        @Parameter(description = "Площадь площадки максимальная") @RequestParam fullAreaMax: Int?,
        @Parameter(description = "Минимальный этаж") @RequestParam levelNumberMin: Int?,
        @Parameter(description = "Максимальный этаж") @RequestParam levelNumberMax: Int?,
        @Parameter(description = "Наличие парковки") @RequestParam parking: Boolean?,
        @Parameter(description = "Страница на фронте") @RequestParam pageNumber: Long,
        @Parameter(description = "Количество площадок на страницу") @RequestParam limit: Int,
        @Parameter(description = "Сортировка") @RequestParam sortType: PlaceSortType,
        @Parameter(description = "Направление сортировки") @RequestParam sortDirection: PlaceSortDirection,
    ): PlaceListDTO {
        return placeService.getPlaces(
            specialization, capacity, fullAreaMin, fullAreaMax,
            levelNumberMin, levelNumberMax, parking, pageNumber, limit, sortType, sortDirection
        )
    }

    @PreAuthorize("hasRole('LANDLORD')")
    @PostMapping
    fun create(
        @RequestBody newPlaceDTO: NewPlaceDTO,
//        @Parameter(ref = "Фотографии площадки") image: List<MultipartFile>
    ): PlaceDTO {
        return placeService.create(newPlaceDTO)
    }

    @PreAuthorize("hasRole('LANDLORD')")
    @PutMapping("edit")
    fun edit(
        @RequestBody updatePlaceDTO: UpdatePlaceDTO,
//        @Parameter(ref = "Фотографии площадки") image: List<MultipartFile>
    ): PlaceDTO {
        return placeService.edit(updatePlaceDTO)
    }
}
