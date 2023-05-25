package moscow.createdin.backend.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "aki")
@ConstructorBinding
data class AkiProperties(
    val noneSameSiteCookies: Boolean,
    val dataPath: String,
    val maxFileSizeMb: Double,
    val imageUrlPrefix: String,
    val agreementDefaultWebsite: String
)
