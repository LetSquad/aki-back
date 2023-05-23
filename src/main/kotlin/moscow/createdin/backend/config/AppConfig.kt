package moscow.createdin.backend.config

import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import moscow.createdin.backend.config.properties.AkiSecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.thymeleaf.ITemplateEngine
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver
import java.security.Key
import javax.crypto.spec.SecretKeySpec

@Configuration
class AppConfig(private val securityProperties: AkiSecurityProperties) {

    @Bean
    fun jwtPrivateKey(): Key {
        return SecretKeySpec(securityProperties.keySecret.toByteArray(), SignatureAlgorithm.HS512.jcaName)
    }

    @Bean
    fun jwtParser(jwtPrivateKey: Key): JwtParser = Jwts.parserBuilder()
        .setSigningKey(jwtPrivateKey)
        .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun templateResolver(): ITemplateResolver {
        return ClassLoaderTemplateResolver().apply {
            suffix = ".html"
            templateMode = TemplateMode.HTML
        }
    }

    @Bean
    fun templateEngine(templateResolver: ITemplateResolver): ITemplateEngine {
        val templateEngine = TemplateEngine()
        templateEngine.setTemplateResolver(templateResolver)
        return templateEngine
    }
}
