package moscow.createdin.backend.service

import moscow.createdin.backend.config.properties.AkiProperties
import moscow.createdin.backend.exception.UserPasswordResetException
import moscow.createdin.backend.model.dto.ResetUserPasswordTO
import moscow.createdin.backend.model.entity.UserPasswordResetTokenEntity
import moscow.createdin.backend.repository.UserPasswordResetTokenRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class ResetPasswordService(
    private val akiProperties: AkiProperties,
    private val userPasswordResetRepository: UserPasswordResetTokenRepository,
    private val userService: UserService,
    private val mailService: MailService
) {

    fun resetPassword(email: String) {
        val userId = userService.getUserByEmail(email).id!!
        val token = UUID.randomUUID().toString()

        savePasswordResetToken(userId, token)

        mailService.sendResetEmail(email, token)
    }

    private fun savePasswordResetToken(userId: Long, token: String) {
        val userPasswordReset = UserPasswordResetTokenEntity(
            id = null,
            user = userService.getUserById(userId),
            resetToken = token,
            expire = calculateExpiryDate(akiProperties.resetPasswordExpirationMin)
        )
        userPasswordResetRepository.save(userPasswordReset)
    }

    private fun calculateExpiryDate(expiryTimeInMinutes: Int): Instant {
        val now = Instant.now()
        return now.plus(expiryTimeInMinutes.toLong(), ChronoUnit.MINUTES)
    }

    fun isTokenValid(token: String): Boolean {
        val expiryDate = getByToken(token).expire
        val now = Instant.now()
        return now < expiryDate
    }

    private fun getByToken(token: String): UserPasswordResetTokenEntity {
        return userPasswordResetRepository.findByToken(token)
    }

    fun changePassword(userPasswordTO: ResetUserPasswordTO) {
        if (!isTokenValid(userPasswordTO.token)) {
            throw UserPasswordResetException("Reset password token ${userPasswordTO.token} is not valid")
        }
        val user = getByToken(userPasswordTO.token).user
        userService.changePassword(user, userPasswordTO.password)
    }
}
