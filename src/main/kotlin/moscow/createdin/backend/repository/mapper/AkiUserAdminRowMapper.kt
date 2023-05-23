package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.AkiUserEntity
import moscow.createdin.backend.model.enums.UserRole
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class AkiUserAdminRowMapper : RowMapper<AkiUserEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): AkiUserEntity = AkiUserEntity(
        id = rs.getLong("id"),
        email = rs.getString("email"),
        password = rs.getString("password"),
        role = UserRole.ADMIN.name,
        firstName = rs.getString("first_name"),
        lastName = rs.getString("last_name"),
        middleName = rs.getString("middle_name"),
        phone = rs.getString("phone"),
        userImage = rs.getString("user_image"),
        inn = null,
        organization = null,
        logoImage = null,
        jobTitle = rs.getString("job_title"),
        isActivated = rs.getBoolean("is_activated"),
        activationCode = rs.getString("activation_code"),
        isBanned = rs.getBoolean("is_banned"),
        admin = null,
        banReason = rs.getString("ban_reason"),
        type = rs.getString("user_type"),
    )
}
