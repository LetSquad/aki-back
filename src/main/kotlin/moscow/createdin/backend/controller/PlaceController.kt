package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import moscow.createdin.backend.model.dto.BanDataRequestDTO
import moscow.createdin.backend.model.dto.place.NewPlaceDTO
import moscow.createdin.backend.model.dto.place.PlaceDTO
import moscow.createdin.backend.model.dto.place.PlaceListDTO
import moscow.createdin.backend.model.dto.place.UpdatePlaceDTO
import moscow.createdin.backend.model.enums.PlaceSortDirection
import moscow.createdin.backend.model.enums.PlaceSortType
import moscow.createdin.backend.model.enums.SpecializationType
import moscow.createdin.backend.service.PlaceService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.server.UnsupportedMediaTypeStatusException
import java.time.LocalDate
import javax.servlet.http.HttpServletRequest

@Tag(name = "Методы работы с площадками")
@RestController
@RequestMapping("/api/places")
class PlaceController(
    private val placeService: PlaceService
) {

    @Operation(
        summary = "Получение каталога мест",
        description = "По различным фильтрам можно получить площадки"
    )
    @GetMapping
    fun getPlaces(
        @Parameter(description = "Специализация площадки") @RequestParam specialization: List<SpecializationType>?,
        @Parameter(description = "Рейтинг") @RequestParam rating: Int?,
        @Parameter(description = "Минимальная цена площадки") @RequestParam priceMin: Int?,
        @Parameter(description = "Максимальная цена площадки") @RequestParam priceMax: Int?,
        @Parameter(description = "Минимальная вместимость площадки") @RequestParam capacityMin: Int?,
        @Parameter(description = "Максимальная вместимость площадки") @RequestParam capacityMax: Int?,
        @Parameter(description = "Площадь площадки минимальная") @RequestParam squareMin: Int?,
        @Parameter(description = "Площадь площадки максимальная") @RequestParam squareMax: Int?,
        @Parameter(description = "Минимальный этаж") @RequestParam levelNumberMin: Int?,
        @Parameter(description = "Максимальный этаж") @RequestParam levelNumberMax: Int?,
        @Parameter(description = "Наличие парковки") @RequestParam withParking: Boolean?,
        @Parameter(description = "Начальная дата бронирования") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") dateFrom: LocalDate?,
        @Parameter(description = "Конечная дата бронирования") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") dateTo: LocalDate?,
        @Parameter(description = "Станции метро") @RequestParam metroStations: List<String>?,

        @Parameter(description = "Страница на фронте") @RequestParam pageNumber: Long,
        @Parameter(description = "Количество площадок на страницу") @RequestParam limit: Int,
        @Parameter(description = "Сортировка") @RequestParam sortType: PlaceSortType,
        @Parameter(description = "Направление сортировки") @RequestParam sortDirection: PlaceSortDirection,
    ): PlaceListDTO {
        return placeService.getPlaces(
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
            sortType,
            sortDirection
        )
    }

    @Operation(
        summary = "Создание площадки"
    )
    @PreAuthorize("hasRole('LANDLORD')")
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun create(
        @Parameter(
            ref = "Модель данных площадки",
            schema = Schema(type = "string", format = "binary")
        )
        @RequestPart place: NewPlaceDTO,

        request: HttpServletRequest
    ): PlaceDTO {
        return placeService.create(place, request.retrieveImages())
    }

    @Operation(
        summary = "Бан площадки администратором"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("ban")
    fun ban(@RequestBody banRequestDTO: BanDataRequestDTO): PlaceDTO {
        return placeService.ban(banRequestDTO.data)
    }

    @Operation(
        summary = "Подтверждение площадки администратором"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("{id}/approve")
    fun approve(@PathVariable id: Long): PlaceDTO {
        return placeService.approve(id)
    }

    @Operation(
        summary = "Изменение площадки"
    )
    @PreAuthorize("hasRole('LANDLORD')")
    @PutMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun edit(
        @Parameter(
            ref = "Модель данных площадки",
            schema = Schema(type = "string", format = "binary")
        )
        @RequestPart place: UpdatePlaceDTO,

        request: HttpServletRequest
    ): PlaceDTO {
        return placeService.edit(place, request.retrieveImages())
    }

    @Operation(
        summary = "Получение площадки по ее идентификатору"
    )
    @GetMapping("{id}")
    fun get(@Parameter(description = "ID площадки") @PathVariable id: Long): PlaceDTO {
        return placeService.get(id)
    }

    @Operation(
        summary = "Получение своих площадок в зависимости от роли",
        description = "Для админа - это неподтвержденные площадки, для пользователя - его избранные, для арендодателя - его площадки"
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('LANDLORD')")
    @GetMapping("my")
    fun getCurrentUserPlaces(
        @Parameter(description = "Страница на фронте") @RequestParam pageNumber: Long,
        @Parameter(description = "Количество площадок на страницу") @RequestParam limit: Int
    ): PlaceListDTO {
        return placeService.getCurrentUserPlaces(pageNumber, limit)
    }

    @Operation(
        summary = "Удаление площадки"
    )
    @PreAuthorize("hasRole('LANDLORD')")
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) {
        placeService.delete(id)
    }

    @Operation(
        summary = "Добавление площадки в избранное"
    )
    @PostMapping("{id}/favorite")
    fun createFavorite(@Parameter(description = "ID площадки") @PathVariable id: Long): PlaceDTO {
        return placeService.createFavorite(id)
    }

    @Operation(
        summary = "Удаление площадки из избранного"
    )
    @DeleteMapping("{id}/favorite")
    fun deleteFavorite(@Parameter(description = "ID площадки") @PathVariable id: Long): PlaceDTO {
        return placeService.deleteFavorite(id)
    }

    private fun HttpServletRequest.retrieveImages(): Collection<MultipartFile> {
        val images: Collection<MultipartFile> = if (this is MultipartHttpServletRequest) {
            fileMap.values
                .filter { it.name != "place" }
        } else {
            emptyList()
        }
        for (image in images) {
            if (image.contentType != MediaType.IMAGE_JPEG_VALUE && image.contentType != MediaType.IMAGE_PNG_VALUE) {
                throw UnsupportedMediaTypeStatusException("Media type ${image.contentType} is not supported")
            }
        }
        return images
    }
}
