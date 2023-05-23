package moscow.createdin.backend.controller

import moscow.createdin.backend.model.dto.CreateRentSlotRequestDTOList
import moscow.createdin.backend.model.dto.DeleteRentSlotRequestDTOList
import moscow.createdin.backend.model.dto.place.PlaceDTO
import moscow.createdin.backend.service.RentSlotService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/rent-slot")
class RentSlotController(
    private val rentSlotService: RentSlotService
) {

    @PreAuthorize("hasRole('LANDLORD')")
    @PostMapping
    fun create(@RequestBody req: CreateRentSlotRequestDTOList): PlaceDTO {
        return rentSlotService.create(req.rentSlots)
    }

    @PreAuthorize("hasRole('LANDLORD')")
    @DeleteMapping
    fun delete(@RequestBody req: DeleteRentSlotRequestDTOList): PlaceDTO {
        return rentSlotService.delete(req.rentSlotIds)
    }
}
