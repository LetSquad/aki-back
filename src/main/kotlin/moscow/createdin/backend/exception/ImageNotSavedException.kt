package moscow.createdin.backend.exception

class ImageNotSavedException(userId: Long?, cause: Throwable?) :
    RuntimeException("Image could not be saved for this userId = $userId", cause)

