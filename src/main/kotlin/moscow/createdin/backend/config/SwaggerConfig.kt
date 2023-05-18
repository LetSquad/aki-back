package moscow.createdin.backend.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI().info(apiInfo())
            .addServersItem(Server().url("/"))
    }

    private fun apiInfo(): Info = Info().title("AKI Backend")
        .description("Агрегатор площадок и услуг креативных индрустрий Москвы")
        .version("1.0")
        .contact(
            Contact().name("LetSquad")
                .email("")
                .url("")
        )
}
