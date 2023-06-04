package moscow.createdin.backend.repository

interface FavoritePlaceRepository {

    fun save(placeId: Long, userId: Long?): Long

    fun delete(placeId: Long, userId: Long?)
}
