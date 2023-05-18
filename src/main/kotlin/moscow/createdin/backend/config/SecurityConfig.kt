package moscow.createdin.backend.config

import moscow.createdin.backend.filter.JwtRequestFilter
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import javax.annotation.PostConstruct

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val serverProperties: ServerProperties,
    private val jwtRequestFilter: JwtRequestFilter,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder
) {

    @PostConstruct
    fun init() {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
    }

    @Bean
    fun akiBackFilterChain(http: HttpSecurity): SecurityFilterChain {
        val config = http.csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/auth", "/auth/refresh").permitAll()
            .antMatchers(HttpMethod.GET, "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterAfter(jwtRequestFilter, BasicAuthenticationFilter::class.java)
            .exceptionHandling()
            .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .accessDeniedHandler { _, r, _ -> r.status = HttpStatus.FORBIDDEN.value() }
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

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
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
