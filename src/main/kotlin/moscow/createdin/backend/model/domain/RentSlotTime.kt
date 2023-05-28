package moscow.createdin.backend.model.domain

import java.time.Instant

data class RentSlotTime(
    val timeStart: Instant,
    val timeEnd: Instant,
)
