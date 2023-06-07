package moscow.createdin.backend.service

import moscow.createdin.backend.config.properties.AkiProperties
import moscow.createdin.backend.getLogger
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.ComponentList
import net.fortuna.ical4j.model.Property
import net.fortuna.ical4j.model.PropertyList
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.CalScale
import net.fortuna.ical4j.model.property.Description
import net.fortuna.ical4j.model.property.DtStart
import net.fortuna.ical4j.model.property.ProdId
import net.fortuna.ical4j.model.property.Summary
import net.fortuna.ical4j.model.property.Uid
import net.fortuna.ical4j.model.property.Version
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.util.*
import javax.activation.DataHandler
import javax.mail.BodyPart
import javax.mail.Message
import javax.mail.Multipart
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource


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
    fun sendRentEmailToRenter(
        email: String, timeStart: Instant,
        timeEnd: Instant, renter: String, placeName: String
    ) {
        val message =
            """
               Привет, $email!
               Бронь по площадке успешно создана на сервисе АКИ "Агрегатор креативных пространств Москвы"!
               
               Зайдите в личный кабинет для просмотра деталей
            """

        sendMimeMessage(email, "Уведомление о брони площадки", message, timeStart, timeEnd, renter, placeName)
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
        try {
            val mailMessage = SimpleMailMessage()
            mailMessage.from = mailProperties.username
            mailMessage.setTo(emailTo)
            mailMessage.subject = subject
            mailMessage.text = message
            mailSender.send(mailMessage)
        } catch (ignore: Exception) {
            log.warn("mail send fail", ignore)
        }
    }

    private fun sendMimeMessage(
        emailTo: String, subject: String, message: String, timeStart: Instant,
        timeEnd: Instant, renter: String, placeName: String
    ) {
        try {
            val mimeMessage: MimeMessage = mailSender.createMimeMessage()

            mimeMessage.addHeaderLine("method=REQUEST")
            mimeMessage.addHeaderLine("charset=UTF-8")
            mimeMessage.addHeaderLine("component=VEVENT")

            mimeMessage.setFrom(mailProperties.username)
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo))
            mimeMessage.subject = subject
            mimeMessage.setText(message)
            mimeMessage.setHeader("Content-class", "urn:content-classes:calendarmessage")

            val content: String = generateCalendar(timeStart, timeEnd, renter, placeName)
            // Create the message part
            val messageBodyPart: BodyPart = MimeBodyPart()

            // Fill the message
            messageBodyPart.setHeader("Content-Class", "urn:content-  classes:calendarmessage")
            messageBodyPart.setHeader("Content-ID", "calendar_message")
            messageBodyPart.dataHandler =
                DataHandler(ByteArrayDataSource(content, "text/calendar;method=REQUEST")) // very important

            val multipart: Multipart = MimeMultipart()
            multipart.addBodyPart(messageBodyPart)
            mimeMessage.setContent(multipart)

            mailSender.send(mimeMessage)
        } catch (ignore: Exception) {
            log.warn("mail send fail", ignore)
        }
    }

    private fun generateCalendar(timeStart: Instant, timeEnd: Instant, renter: String, placeName: String): String {
        val icsCalendar = Calendar()
        val property = mutableListOf<Property>()
        property.add(ProdId(placeName))
        property.add(CalScale(CalScale.VALUE_GREGORIAN))
        val version = Version()
        version.value = Version.VALUE_2_0
        property.add(version)
        val propertyList = PropertyList(property)
        icsCalendar.propertyList = propertyList

        val eventList = mutableListOf<VEvent>()
        eventList.add(generateEvent(timeStart, timeEnd, renter, placeName))

        icsCalendar.componentList = ComponentList(eventList)
        return icsCalendar.toString()
    }

    private fun generateEvent(timeStart: Instant, timeEnd: Instant, renter: String, placeName: String): VEvent {
        val vEvent = VEvent()
        val property = mutableListOf<Property>()
        val dateStart = Date.from(timeStart)
        val dateEnd = Date.from(timeEnd)

        val localDateStart = dateStart.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        val localDateEnd = dateEnd.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        property.add(Uid(UUID.randomUUID().toString()))

        property.add(
            DtStart(
                localDateStart
            )
        )
        property.add(net.fortuna.ical4j.model.property.Duration(Duration.between(localDateStart, localDateEnd)))

        //Add title and description
        property.add(Summary("Бронь для площадки $placeName"))
        property.add(Description(" Арендатор - $renter"))

        val propertyList = PropertyList(property)
        vEvent.propertyList = propertyList
        return vEvent
    }

    companion object {
        private val log = getLogger<MailService>()
    }
}
