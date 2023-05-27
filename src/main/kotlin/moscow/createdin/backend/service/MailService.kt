package moscow.createdin.backend.service

import moscow.createdin.backend.config.properties.AkiProperties
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MailService(
    private val properties: AkiProperties,
    private val mailProperties: MailProperties,
    private val mailSender: JavaMailSender
) {

    fun sendWelcomeEmail(email: String, code: String?) {
        val message =
            """
               Привет, $email!
               Добро пожаловать на сервис АКИ "Агрегатор креативных пространств Москвы". 
               Для подтверждения аккаунта перейдите по ссылке: ${properties.url}/api/user/activate/$code
            """

        sendMail(email, "Код активации", message)
    }

    // TODO придумать нормальный текст
    fun sendRentEmailToRenter(email: String) {
        val message =
            """
               Привет, $email!
               Бронь по площадке успешно создана на сервисе АКИ "Агрегатор креативных пространств Москвы"!
               
               Зайдите в личный кабинет для просмотра деталей
            """

        sendMail(email, "Уведомление о брони площадки", message)
    }

    // TODO придумать нормальный текст
    fun sendRentEmailToLandlord(email: String) {
        val message =
            """
               Привет, $email!
               Уведомляем вас о брони по вашей площадке на сервисе АКИ "Агрегатор креативных пространств Москвы"!
               
               Зайдите в личный кабинет для просмотра деталей
            """

        sendMail(email, "Уведомление о брони площадки", message)
    }

    fun sendResetEmail(email: String, token: String?) {
        val message =
            """
             Вы получили это письмо, так как запросили сброс пароля аккаунта на сервисе АКИ "Агрегатор креативных пространств Москвы".
             
             Если это были не вы - не обращайте внимание на это письмо
             
             Для сброса пароля перейдите по ссылке: ${properties.url}/api/user/validate-reset-password-token/$token
             Если вы не перейдете в течении трех часов, ссылка станет недоступной
             
             С уважением, команда АКИ
             """

        sendMail(email, "Сброс пароля", message)
    }

    private fun sendMail(emailTo: String, subject: String, message: String) {
        val mailMessage = SimpleMailMessage()
        mailMessage.from = mailProperties.username
        mailMessage.setTo(emailTo)
        mailMessage.subject = subject
        mailMessage.text = message
        mailSender.send(mailMessage)
    }
}
