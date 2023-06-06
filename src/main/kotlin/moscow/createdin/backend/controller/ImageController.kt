package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.tags.Tag
import moscow.createdin.backend.service.FilesystemService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Tag(name = "Методы работы с изображениями")
@RestController
@RequestMapping("/api/image")
class ImageController(private val filesystemService: FilesystemService) {

    @Operation(
        summary = "Сохранение изображения"
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun postImage(
        @Parameter(ref = "Фотография", content = [Content(mediaType = MediaType.IMAGE_JPEG_VALUE)])
        @RequestPart(required = true) image: MultipartFile
    ): String? {
        return filesystemService.saveImage(image)
    }

    @Operation(
        summary = "Удаление изображения"
    )
    @DeleteMapping
    fun deleteImage(@RequestParam imageName: String) {
        filesystemService.deleteFile(imageName)
    }
}
