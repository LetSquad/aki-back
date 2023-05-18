package moscow.createdin.backend.context

data class UserContextData(
    override val userEmail: String,
    override val userRole: String
) : UserContext
