package moscow.createdin.backend.service

import moscow.createdin.backend.config.properties.AkiProperties
import moscow.createdin.backend.getLogger
import org.apache.tomcat.util.http.fileupload.FileUploadException
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException
import org.springframework.boot.system.ApplicationHome
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID

@Service
class FilesystemService(properties: AkiProperties) {

    private val maxFileSizeBytes: Long = (properties.maxFileSizeMb * 1024 * 1024).toLong()

    final val dataPath: Path = ApplicationHome(FilesystemService::class.java)
        .dir
        .toPath()
        .resolve(properties.dataPath)

    init {
        log.info("Static data path: ${dataPath.toAbsolutePath()}")
        if (!Files.exists(dataPath)) {
            Files.createDirectory(dataPath)
        }
    }

    fun saveImage(image: MultipartFile): String {
        if (image.isEmpty) throw FileUploadException("Received empty image")

        if (MediaType.IMAGE_JPEG_VALUE != image.contentType &&
            MediaType.IMAGE_PNG_VALUE != image.contentType
        ) {
            throw InvalidContentTypeException(image.contentType)
        }
        if (image.size > maxFileSizeBytes) {
            throw FileSizeLimitExceededException(
                "Image too large (${image.size} bytes). Limit is $maxFileSizeBytes bytes",
                image.size,
                maxFileSizeBytes
            )
        }

        val imageName = UUID.randomUUID().toString() + "." + image.contentType?.removePrefix("image/")
        saveMultipartFile(imageName, image)
        return imageName
    }

    fun deleteFile(filename: String) {
        try {
            Files.delete(dataPath.resolve(filename))
        } catch (e: IOException) {
            log.error("Error deleting a file", e)
        }
    }

    private fun saveMultipartFile(filename: String, file: MultipartFile) {
        Files.createFile(dataPath.resolve(filename))
            .let { Files.write(it, file.bytes) }
    }

    companion object {
        private val log = getLogger<FilesystemService>()
    }
}
