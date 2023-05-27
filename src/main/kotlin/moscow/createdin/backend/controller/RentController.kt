package moscow.createdin.backend.controller

import moscow.createdin.backend.model.dto.CreateRentRequestDTO
import moscow.createdin.backend.model.dto.RentDTO
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
@RequestMapping("/api/rent")
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
    fun getAll(): List<RentDTO> {
        return rentService.getAll()
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
}
