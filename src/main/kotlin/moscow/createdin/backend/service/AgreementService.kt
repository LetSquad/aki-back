package moscow.createdin.backend.service

import moscow.createdin.backend.config.properties.AkiProperties
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
    private val filesystemService: FilesystemService
) {

    fun createAgreement(organization: String, logo: String, website: String?, agreementNumber: Long): String {
        val htmlContent: String = generateAgreementHtml(organization, logo, website, agreementNumber)
        return filesystemService.saveHtmlContentAsPdf(htmlContent)
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
        val image: ByteArray = resourceLoader.getResource(path)
            .inputStream
            .use { it.readAllBytes() }
        setVariable(name, Base64.getEncoder().encodeToString(image))
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.YYYY")
    }
}
