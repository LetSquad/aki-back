package moscow.createdin.backend.service.auth

import moscow.createdin.backend.mapper.UserMapper
import moscow.createdin.backend.service.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AkiUserDetailsService(
    private val userMapper: UserMapper,
    private val userService: UserService
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails = userService.getUserByEmail(username)
        .let { userMapper.domainToDetailsDomain(it) }
}
