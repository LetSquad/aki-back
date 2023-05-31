package moscow.createdin.backend.controller

import moscow.createdin.backend.model.dto.AgreementDTO
import moscow.createdin.backend.model.dto.CreateAgreementRequestDTO
import moscow.createdin.backend.service.AgreementService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/agreement")
class AgreementController(private val agreementService: AgreementService) {

    @PreAuthorize("hasRole('RENTER')")
    @PostMapping
    fun postAgreement(@RequestBody createAgreementRequest: CreateAgreementRequestDTO): AgreementDTO {
        return agreementService.createAgreement(createAgreementRequest)
    }
}
