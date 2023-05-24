package moscow.createdin.backend.repository

import moscow.createdin.backend.model.entity.AreaEntity

interface AreaRepository {

    fun findById(id: Long): AreaEntity

    fun save(area: AreaEntity): Long

    fun update(area: AreaEntity)

    fun findByUserId(userId: Long): List<AreaEntity>

}
