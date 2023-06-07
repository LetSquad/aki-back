package moscow.createdin.backend.model.dto

import java.io.InputStream

data class CalendarFileDTO(
    val file: InputStream,
    val filename: String
)
