package moscow.createdin.backend.repository.mapper

import java.sql.ResultSet

fun ResultSet.getLongOrNull(columnLabel: String): Long? {
    val columnValue: Long = getLong(columnLabel)
    return if (wasNull()) null else columnValue
}
