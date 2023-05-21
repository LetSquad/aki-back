package moscow.createdin.backend.exception

class UserAlreadyExistsException(uniqueField: String, fieldValue: String)
    : AkiException("User with $uniqueField = $fieldValue alread exists")
