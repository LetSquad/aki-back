package moscow.createdin.backend.repository.mapper

import moscow.createdin.backend.model.entity.UserPasswordResetTokenEntity
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class UserPasswordResetTokenRowMapper(
    private val akiUserRowMapper: AkiUserRowMapper,
) : RowMapper<UserPasswordResetTokenEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): UserPasswordResetTokenEntity = UserPasswordResetTokenEntity(
        id = rs.getLong("id"),
        user = akiUserRowMapper.mapRow(rs, rowNum),
        resetToken = rs.getString("reset_token"),
        expire = rs.getTimestamp("expire")
    )
}
