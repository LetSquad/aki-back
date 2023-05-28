package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Parameter
import moscow.createdin.backend.model.dto.BanRequestDTO
import moscow.createdin.backend.model.dto.CreateRentRequestDTO
import moscow.createdin.backend.model.dto.RentDTO
import moscow.createdin.backend.model.dto.RentListDTO
import moscow.createdin.backend.service.RentService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/rents")
class RentController(
    private val rentService: RentService
) {

    @PreAuthorize("hasRole('RENTER')")
    @PostMapping
    fun create(@RequestBody req: CreateRentRequestDTO): RentDTO {
        return rentService.create(req)
    }

    @PreAuthorize("hasRole('RENTER')")
    @GetMapping
    fun getAll(@Parameter(description = "Страница на фронте") @RequestParam pageNumber: Long,
               @Parameter(description = "Количество площадок на страницу") @RequestParam limit: Int
    ): RentListDTO {
        return rentService.getAll(pageNumber, limit)
    }

    @GetMapping("{id}")
    fun get(@RequestParam id: Long): RentDTO {
        return rentService.get(id)
    }

    @PreAuthorize("hasRole('RENTER')")
    @DeleteMapping
    fun delete(@RequestBody id: Long) {
        rentService.delete(id)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("ban")
    fun ban(@RequestBody banRequestDTO: BanRequestDTO): RentDTO {
        return rentService.ban(banRequestDTO)
    }
}
