package moscow.createdin.backend.exception

abstract class AkiException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
