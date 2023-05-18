package moscow.createdin.backend.filter

import moscow.createdin.backend.context.UserContext
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter(private val userContext: UserContext, ) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val auth: Authentication? = createAuthentication(userContext, request)
        if (auth != null) {
            SecurityContextHolder.getContext().authentication = auth
        }

        chain.doFilter(request, response)
    }

    private fun createAuthentication(userContext: UserContext, request: HttpServletRequest): Authentication? {
        if (userContext.userEmail == null || userContext.userRole == null) return null

        return UsernamePasswordAuthenticationToken(
            userContext.userEmail,
            null,
            listOf(SimpleGrantedAuthority(userContext.userRole))
        ).apply {
            details = WebAuthenticationDetailsSource().buildDetails(request)
        }
    }
}
