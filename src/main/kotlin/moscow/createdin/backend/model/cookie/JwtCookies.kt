package moscow.createdin.backend.model.cookie

import java.time.Duration
import javax.servlet.http.Cookie

data class JwtCookies(
    private val authToken: String,
    private val refreshToken: String,
    private val cookiesValidity: Duration,
    private val isSecure: Boolean
) {

    fun retrieveAuthCookie(): Cookie {
        return Cookie(CookieName.AUTH_TOKEN, authToken).apply {
            maxAge = Math.toIntExact(cookiesValidity.seconds)
            secure = isSecure
            isHttpOnly = true
            path = "/"
        }
    }

    fun retrieveRefreshCookie(): Cookie {
        return Cookie(CookieName.REFRESH_TOKEN, refreshToken).apply {
            maxAge = Math.toIntExact(cookiesValidity.seconds)
            secure = isSecure
            isHttpOnly = true
            path = "/"
        }
    }
}
