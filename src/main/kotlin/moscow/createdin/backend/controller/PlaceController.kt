package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import moscow.createdin.backend.model.dto.BanDataRequestDTO
import moscow.createdin.backend.model.dto.place.NewPlaceDTO
import moscow.createdin.backend.model.dto.place.PlaceDTO
import moscow.createdin.backend.model.dto.place.PlaceListDTO
import moscow.createdin.backend.model.dto.place.UpdatePlaceDTO
import moscow.createdin.backend.model.enums.PlaceSortDirection
import moscow.createdin.backend.model.enums.PlaceSortType
import moscow.createdin.backend.service.PlaceService
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.server.UnsupportedMediaTypeStatusException
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/places")
class PlaceController(private val placeService: PlaceService) {

    @GetMapping
    fun getPlaces(
        @Parameter(description = "Специализация площадки") @RequestParam specialization: String?,
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
        @Parameter(description = "Начальная дата бронирования") @RequestParam dateFrom: Int?,
        @Parameter(description = "Конечная дата бронирования") @RequestParam dateTo: Int?,

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

            pageNumber,
            limit,
            sortType,
            sortDirection
        )
    }

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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("ban")
    fun ban(@RequestBody banRequestDTO: BanDataRequestDTO): PlaceDTO {
        return placeService.ban(banRequestDTO.data)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("{id}/approve")
    fun approve(@PathVariable id: Long): PlaceDTO {
        return placeService.approve(id)
    }

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

    @GetMapping("{id}")
    fun get(@Parameter(description = "ID площадки") @PathVariable id: Long): PlaceDTO {
        return placeService.get(id)
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('LANDLORD')")
    @GetMapping("my")
    fun getCurrentUserPlaces(
        @Parameter(description = "Страница на фронте") @RequestParam pageNumber: Long,
        @Parameter(description = "Количество площадок на страницу") @RequestParam limit: Int
    ): PlaceListDTO {
        return placeService.getCurrentUserPlaces(pageNumber, limit)
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

    @PreAuthorize("hasRole('LANDLORD')")
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) {
        placeService.delete(id)
    }
}
