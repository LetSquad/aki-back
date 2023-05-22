package moscow.createdin.backend.controller

import moscow.createdin.backend.getLogger
import moscow.createdin.backend.model.ErrorCode
import moscow.createdin.backend.model.dto.ErrorDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandlingControllerAdvise {

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception): ErrorDTO {
        log.error("Caught unhandled error", e)
        return ErrorDTO(ErrorCode.UNKNOWN_ERROR, e.message)
    }

    companion object {
        private val log = getLogger<ErrorHandlingControllerAdvise>()
    }
}
