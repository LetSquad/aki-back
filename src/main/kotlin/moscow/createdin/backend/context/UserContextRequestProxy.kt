package moscow.createdin.backend.context

import moscow.createdin.backend.model.cookie.CookieName
import moscow.createdin.backend.service.auth.JwtTokenService
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.util.WebUtils
import javax.servlet.http.HttpServletRequest

@Component
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.INTERFACES)
class UserContextRequestProxy(
    request: HttpServletRequest,
    jwtTokenService: JwtTokenService
) : UserContext {

    private val contextDelegate: UserContext? = jwtTokenService.retrieveUserContext(
        jwtToken = WebUtils.getCookie(request, CookieName.AUTH)?.value
    )

    override val userEmail: String?
        get() = contextDelegate?.userEmail

    override val userRole: String?
        get() = contextDelegate?.userRole
}
