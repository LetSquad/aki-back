package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import moscow.createdin.backend.getLogger
import moscow.createdin.backend.model.dto.CreateRentSlotRequestDTOList
import moscow.createdin.backend.model.dto.DeleteRentSlotRequestDTOList
import moscow.createdin.backend.model.dto.place.PlaceDTO
import moscow.createdin.backend.service.RentSlotService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Методы работы со слотами")
@RestController
@RequestMapping("/api/rentSlots")
class RentSlotController(
    private val rentSlotService: RentSlotService
) {

    @Operation(
        summary = "Создание слотов для возможных аренд"
    )
    @PreAuthorize("hasRole('LANDLORD')")
    @PostMapping
    fun create(@RequestBody req: CreateRentSlotRequestDTOList): ResponseEntity<PlaceDTO> {
        try {
            return ResponseEntity.ok(rentSlotService.create(req.rentSlots))
        } catch (e: Exception) {
            log.warn("Unprocessable create request", e)
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .build()
        }
    }

    @Operation(
        summary = "Удаление слотов для аренды"
    )
    @PreAuthorize("hasRole('LANDLORD')")
    @DeleteMapping
    fun delete(@RequestBody req: DeleteRentSlotRequestDTOList): PlaceDTO {
        return rentSlotService.delete(req.rentSlotIds)
    }

    companion object {
        private val log = getLogger<RentSlotController>()
    }
}
