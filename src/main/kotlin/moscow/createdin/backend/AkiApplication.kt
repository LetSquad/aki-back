package moscow.createdin.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class AkiApplication

fun main(args: Array<String>) {
    runApplication<AkiApplication>(*args)
}
