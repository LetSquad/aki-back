package moscow.createdin.backend

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> getLogger(): Logger = LoggerFactory.getLogger(T::class.java)

fun getLikeName(parameter: String?): String? {
    if (parameter.isNullOrEmpty()) {
        return parameter
    }
    return "%$parameter%"
}
