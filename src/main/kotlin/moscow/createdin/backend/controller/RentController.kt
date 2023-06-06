package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import moscow.createdin.backend.model.dto.BanRequestDTO
import moscow.createdin.backend.model.dto.CreateRentRequestDTO
import moscow.createdin.backend.model.dto.RentDTO
import moscow.createdin.backend.model.dto.RentListDTO
import moscow.createdin.backend.model.dto.RentReviewDTO
import moscow.createdin.backend.model.dto.place.RentDeleteDTO
import moscow.createdin.backend.service.RentService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Методы работы с арендами")
@RestController
@RequestMapping("/api/rents")
class RentController(
    private val rentService: RentService
) {

    @Operation(
        summary = "Создание аренды"
    )
    @PreAuthorize("hasRole('RENTER')")
    @PostMapping
    fun create(@RequestBody req: CreateRentRequestDTO): RentDTO {
        return rentService.create(req)
    }

    @Operation(
        summary = "Получение своих аренд"
    )
    @PreAuthorize("hasRole('RENTER')")
    @GetMapping
    fun getAll(
        @Parameter(description = "Страница на фронте") @RequestParam pageNumber: Long,
        @Parameter(description = "Количество площадок на страницу") @RequestParam limit: Int
    ): RentListDTO {
        return rentService.getAll(pageNumber, limit)
    }

    @Operation(
        summary = "Получение аренды по ее идентификатору"
    )
    @GetMapping("{id}")
    fun get(@PathVariable id: Long): RentDTO {
        return rentService.get(id)
    }

    @Operation(
        summary = "Удаление аренды"
    )
    @PreAuthorize("hasRole('RENTER')")
    @DeleteMapping
    fun delete(@RequestBody req: RentDeleteDTO) {
        rentService.delete(req.rentId)
    }

    @Operation(
        summary = "Бан аренды администратором"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("ban")
    fun ban(@RequestBody banRequestDTO: BanRequestDTO): RentDTO {
        return rentService.ban(banRequestDTO)
    }

    @Operation(
        summary = "Оценивание аренды"
    )
    @PreAuthorize("hasRole('RENTER')")
    @PostMapping("{rentId}/rate")
    fun rate(@PathVariable rentId: Long, @RequestBody rentReviewDTO: RentReviewDTO) {
        rentService.rate(rentId, rentReviewDTO)
    }
}
