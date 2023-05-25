package moscow.createdin.backend.model.dto

data class BanRequestDTO(
    val bannedId: Long,
    val reason: String?
)
