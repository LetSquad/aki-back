package moscow.createdin.backend.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConfigurationProperties(prefix = "aki.security")
@ConstructorBinding
data class AkiSecurityProperties(
    val keySecret: String,
    val authTokenValidity: Duration,
    val refreshTokenValidity: Duration
)
