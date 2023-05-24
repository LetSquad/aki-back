package moscow.createdin.backend.config

import moscow.createdin.backend.config.properties.AkiProperties
import moscow.createdin.backend.service.FilesystemService
import org.apache.tomcat.util.http.Rfc6265CookieProcessor
import org.apache.tomcat.util.http.SameSiteCookies
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val properties: AkiProperties,
    private val filesystemService: FilesystemService
) : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/${properties.imageUrlPrefix}/**")
            .addResourceLocations("file:${filesystemService.dataPath}/")
    }

    @Bean
    @ConditionalOnProperty("aki.none-same-site-cookies")
    fun sameSiteCookiesConfig(): TomcatContextCustomizer = TomcatContextCustomizer { context ->
        val cookieProcessor = Rfc6265CookieProcessor()
        cookieProcessor.setSameSiteCookies(SameSiteCookies.NONE.value)
        context.cookieProcessor = Rfc6265CookieProcessor()
    }
}
