package moscow.createdin.backend.service.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import moscow.createdin.backend.config.properties.AkiSecurityProperties
import moscow.createdin.backend.context.UserContext
import moscow.createdin.backend.context.UserContextData
import moscow.createdin.backend.getLogger
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.time.Duration
import java.util.Date

@Service
class JwtTokenService(
    private val properties: AkiSecurityProperties,
    private val jwtPrivateKey: Key,
    private val jwtParser: JwtParser
) {

    fun generateAuthToken(userDetails: UserDetails): String = generateToken(
        userDetails = userDetails,
        tokenValidity = properties.authTokenValidity,
        withClaims = true
    )

    fun generateRefreshToken(userDetails: UserDetails): String = generateToken(
        userDetails = userDetails,
        tokenValidity = properties.refreshTokenValidity,
        withClaims = false
    )

    fun checkTokenValidOrExpired(jwtToken: String): Boolean = try {
        jwtParser.parse(jwtToken)
        true
    } catch (e: ExpiredJwtException) {
        true
    } catch (e: Exception) {
        log.error("Invalid jwt token", e)
        false
    }

    fun retrieveUserContext(jwtToken: String?): UserContext? {
        if (jwtToken == null) return null

        return try {
            val parsedToken: Jws<Claims> = jwtParser.parseClaimsJws(jwtToken)
            UserContextData(
                userEmail = parsedToken.body.subject,
                userRole = parsedToken.body.get(CLAIM_ROLE, String::class.java)
            )
        } catch (e: ExpiredJwtException) {
            log.warn("Received expired jwt", e)
            null
        }
    }

    fun retrieveSubject(jwtToken: String): String {
        val parsedToken: Jws<Claims> = jwtParser.parseClaimsJws(jwtToken)
        return parsedToken.body.subject
    }

    private fun generateToken(userDetails: UserDetails, tokenValidity: Duration, withClaims: Boolean): String {
        val currentTime: Long = System.currentTimeMillis()

        val jwtBuilder = Jwts.builder()
            .setSubject(userDetails.username)
        if (withClaims) {
            jwtBuilder.claim(CLAIM_ROLE, userDetails.authorities.first().authority)
        }
        return jwtBuilder.setIssuedAt(Date(currentTime))
            .setExpiration(Date(currentTime + tokenValidity.toMillis()))
            .signWith(jwtPrivateKey, SignatureAlgorithm.HS512)
            .compact()
    }

    companion object {
        private const val CLAIM_ROLE = "role"

        private val log = getLogger<JwtTokenService>()
    }
}
