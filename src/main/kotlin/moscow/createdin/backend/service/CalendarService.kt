package moscow.createdin.backend.service

import moscow.createdin.backend.getLogger
import moscow.createdin.backend.model.dto.CalendarFileDTO
import moscow.createdin.backend.model.enums.RentSlotStatusType
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
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.util.*


@Service
class CalendarService(
    private val placeService: PlaceService,
    private val rentSlotService: RentSlotService,
) {
    fun export(placeId: Long): CalendarFileDTO {
        val place = placeService.findById(placeId)
        val rentSlots = rentSlotService.getByPlaceIdFutureSlots(placeId)
        val icsCalendar = Calendar()
        val property = mutableListOf<Property>()
        property.add(ProdId(place.name))
        property.add(CalScale(CalScale.VALUE_GREGORIAN))
        val version = Version()
        version.value = Version.VALUE_2_0
        property.add(version)
        val propertyList = PropertyList(property)
        icsCalendar.propertyList = propertyList

        val eventList = mutableListOf<VEvent>()
        for (slot in rentSlots) {
            val vEvent = createEvent(slot.timeStart, slot.timeEnd, slot.price, slot.status, place.name)
            eventList.add(vEvent)
        }

        icsCalendar.componentList = ComponentList(eventList)
        return CalendarFileDTO(icsCalendar.toString().byteInputStream(), "${place.name}-slots.ics")
    }

    private fun createEvent(
        timeStart: Instant,
        timeEnd: Instant,
        price: BigDecimal,
        status: RentSlotStatusType?,
        name: String
    ): VEvent {
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
        property.add(Summary("Слот для площадки $name"))
        val russianStatus = russianStatus(status)
        property.add(Description(" Цена = $price | Статус брони = $russianStatus"))

        val propertyList = PropertyList(property)
        vEvent.propertyList = propertyList
        return vEvent
    }

    private fun russianStatus(status: RentSlotStatusType?): String {
        return when (status) {
            RentSlotStatusType.BOOKED -> "Забронирован"
            RentSlotStatusType.OPEN -> "Открыт"
            else -> {
                "Неизвестный статус"
            }
        }
    }

    companion object {
        private val log = getLogger<CalendarService>()
    }
}
