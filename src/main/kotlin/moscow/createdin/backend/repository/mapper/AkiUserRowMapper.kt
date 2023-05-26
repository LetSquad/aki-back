package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.AkiUserEntity
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class AkiUserRowMapper(
    private val akiUserAdminRowMapper: AkiUserAdminRowMapper
) : RowMapper<AkiUserEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): AkiUserEntity = AkiUserEntity(
        id = rs.getLong("id"),
        email = rs.getString("email"),
        password = rs.getString("password"),
        role = rs.getString("role"),
        firstName = rs.getString("first_name"),
        lastName = rs.getString("last_name"),
        middleName = rs.getString("middle_name"),
        phone = rs.getString("phone"),
        userImage = rs.getString("user_image"),
        inn = rs.getString("inn"),
        organization = rs.getString("organization"),
        logoImage = rs.getString("logo_image"),
        jobTitle = rs.getString("job_title"),
        isActivated = rs.getBoolean("is_activated"),
        activationCode = rs.getString("activation_code"),
        isBanned = rs.getBoolean("is_banned"),
        admin = if (rs.getLongOrNull("admin_id") == null) { // TODO здесь будет возникать ошибка при попытке смаппить админа
            null
        } else {
            akiUserAdminRowMapper.mapRow(rs, rowNum)
        },
        banReason = rs.getString("ban_reason"),
        type = rs.getString("type"),
    )
}
