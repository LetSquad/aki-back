package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import moscow.createdin.backend.model.dto.CalendarFileDTO
import moscow.createdin.backend.service.CalendarService
import org.apache.commons.compress.utils.IOUtils
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.StandardCharsets

@Tag(name = "Методы работы с календарем")
@RestController
@RequestMapping("/api/calendar")
class CalendarController(private val calendarService: CalendarService) {

    @Operation(
        summary = "Экспорт календаря в формате ics для его импорта в другие системы"
    )
    @PreAuthorize("hasRole('LANDLORD')")
    @PostMapping("{placeId}/export")
    fun export(
        @PathVariable placeId: Long
    ): ResponseEntity<ByteArray> {
        val calendarFileDTO: CalendarFileDTO =
            calendarService.export(placeId)
        val contentDisposition = ContentDisposition.builder("attachment")
            .filename(calendarFileDTO.filename, StandardCharsets.UTF_8)
            .build()
        val httpHeaders = HttpHeaders()
        httpHeaders.contentDisposition = contentDisposition
        return ResponseEntity.ok()
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(IOUtils.toByteArray(calendarFileDTO.file))
    }
}
