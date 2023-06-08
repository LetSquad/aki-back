package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.CoordinatesEntity

interface CoordinatesRepository {

    fun save(coordinates: CoordinatesEntity): Long

    fun delete(id: Long)
}
