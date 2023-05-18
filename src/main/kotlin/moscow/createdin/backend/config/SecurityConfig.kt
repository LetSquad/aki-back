package moscow.createdin.backend.config

import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(private val serverProperties: ServerProperties) {

    @Bean
    fun akiBackFilterChain(http: HttpSecurity): SecurityFilterChain {
        val config = http.csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .authorizeRequests()
            .anyRequest()
            .permitAll()
            .and()

        if (serverProperties.ssl?.isEnabled == true) {
            config.requiresChannel()
                .anyRequest()
                .requiresSecure()
                .and()
        } else {
            config.cors()
                .configurationSource(createUrlBasedCorsConfigurationSource())
                .and()
        }

        return config.build()
    }

    private fun createUrlBasedCorsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.applyPermitDefaultValues()
        corsConfiguration.allowedMethods = listOf("*")
        corsConfiguration.allowedOrigins = listOf("*")
        val ccs = UrlBasedCorsConfigurationSource()
        ccs.registerCorsConfiguration("/**", corsConfiguration)
        return ccs
    }
}
