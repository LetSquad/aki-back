package moscow.createdin.backend.exception

class IncorrectActivationCodeException(activationCode: String)
    : AkiException("There is no activation code = $activationCode")
