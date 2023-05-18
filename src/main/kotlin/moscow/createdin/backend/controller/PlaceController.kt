package moscow.createdin.backend.controller

import moscow.createdin.backend.service.PlaceService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/place")
class PlaceController(private val placeService: PlaceService) {

    @GetMapping
    fun getPlaces(): List<String> {
        return placeService.getPlaces()
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun postPlace(@RequestBody place: String) {
        placeService.addPlace(place)
    }
}
