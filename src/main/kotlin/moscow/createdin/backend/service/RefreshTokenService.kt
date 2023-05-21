package moscow.createdin.backend.service

import moscow.createdin.backend.repository.UserRefreshTokenRepository
import org.springframework.stereotype.Service

@Service
class RefreshTokenService(private val refreshTokenRepository: UserRefreshTokenRepository) {

    fun isRefreshTokenValid(email: String, refreshToken: String): Boolean {
        return refreshTokenRepository.findByEmail(email) == refreshToken
    }

    fun initUser(email: String) {
        refreshTokenRepository.save(email)
    }

    fun updateRefreshToken(email: String, refreshToken: String) {
        refreshTokenRepository.update(email, refreshToken)
    }
}
