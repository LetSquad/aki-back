package moscow.createdin.backend.controller

import moscow.createdin.backend.service.PlaceService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/places")
class PlaceController(private val placeService: PlaceService) {

    @PreAuthorize("hasRole('LANDLORD')")
    @GetMapping("my")
    fun getCurrentLandlordPlaces(): List<String> {
        return placeService.getPlaces()
    }

    @PreAuthorize("hasRole('LANDLORD')")
    @PostMapping
    fun create(@RequestBody place: String) {
        placeService.create(place)
    }
}
