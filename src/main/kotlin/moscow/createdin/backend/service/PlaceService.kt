package moscow.createdin.backend.service

import org.springframework.stereotype.Service

@Service
class PlaceService {

    fun getPlaces(): List<String> {
        return listOf("Винзавод")
    }

    fun create(place: String) = Unit
}
