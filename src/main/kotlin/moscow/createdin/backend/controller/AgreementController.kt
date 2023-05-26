package moscow.createdin.backend.controller

import moscow.createdin.backend.service.AgreementService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/agreement")
class AgreementController(private val agreementService: AgreementService) {

    @PostMapping
    fun postAgreement(
        @RequestParam organization: String,
        @RequestParam logo: String,
        @RequestParam(required = false) website: String?,
        @RequestParam agreementNumber: Long
    ): String {
        return agreementService.createAgreement(organization, logo, website, agreementNumber)
    }
}
