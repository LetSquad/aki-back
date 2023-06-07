package moscow.createdin.backend.exception

import moscow.createdin.backend.model.enums.RentSlotStatus

class RentSlotBookingException(rentSlotId: Long?, rentSlotStatus: RentSlotStatus)
    : AkiException("Can't book rent slot with id = $rentSlotId and status = $rentSlotStatus")
