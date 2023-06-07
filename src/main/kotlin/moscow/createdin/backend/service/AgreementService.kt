package moscow.createdin.backend.service

import moscow.createdin.backend.config.properties.AkiProperties
import moscow.createdin.backend.getLogger
import moscow.createdin.backend.model.domain.place.Place
import moscow.createdin.backend.model.dto.AgreementDTO
import moscow.createdin.backend.model.dto.CreateAgreementRequestDTO
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.thymeleaf.ITemplateEngine
import org.thymeleaf.context.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Base64

@Service
class AgreementService(
    private val properties: AkiProperties,
    private val resourceLoader: ResourceLoader,
    private val templateEngine: ITemplateEngine,
    private val placeService: PlaceService,
    private val filesystemService: FilesystemService
) {

    fun createAgreement(createAgreementRequest: CreateAgreementRequestDTO): AgreementDTO {
        val place: Place = placeService.getDomain(createAgreementRequest.placeId)

        val htmlContent: String = generateAgreementHtml(
            place.user.organization ?: "",
            place.user.logoImage ?: place.user.userImage ?: "",
            null,
            (1..3000L).random()
        )
        val agreementPath: String = filesystemService.saveHtmlContentAsPdf(htmlContent)

        return AgreementDTO(
            placeId = createAgreementRequest.placeId,
            agreement = "${properties.url}/${properties.imageUrlPrefix}/$agreementPath"
        )
    }

    private fun generateAgreementHtml(organization: String, logo: String, website: String?, agreementNumber: Long): String {
        val context = Context()
        context.setVariable("agreementNumber", agreementNumber)
        context.setVariable("date", LocalDate.now().format(DATE_FORMATTER))
        context.setVariable("organization", organization)
        context.setVariable("website", website ?: properties.agreementDefaultWebsite)
        context.setImage("imgLogo", "file:${filesystemService.dataPath.resolve(logo)}")
        context.setImage("imgVisa", "classpath:template/image/visa.png")
        context.setImage("imgMc", "classpath:template/image/mc.png")
        context.setImage("imgMir", "classpath:template/image/mir.png")

        return templateEngine.process("template/agreement_template", context)
    }

    private fun Context.setImage(name: String, path: String) {
        val image: ByteArray? = try {
            resourceLoader.getResource(path)
                .inputStream
                .use { it.readAllBytes() }
        } catch (e: Exception) {
            log.error("Can't read image", e)
            null
        }
        setVariable(name, image?.let { Base64.getEncoder().encodeToString(image) } ?: "")
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.YYYY")

        private val log = getLogger<AgreementService>()
    }
}
