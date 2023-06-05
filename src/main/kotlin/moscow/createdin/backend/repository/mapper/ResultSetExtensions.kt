package moscow.createdin.backend.repository.mapper

import java.sql.Date
import java.sql.ResultSet

fun ResultSet.getIntOrNull(columnLabel: String): Int? {
    val columnValue: Int = getInt(columnLabel)
    return if (wasNull()) null else columnValue
}

fun ResultSet.getLongOrNull(columnLabel: String): Long? {
    val columnValue: Long = getLong(columnLabel)
    return if (wasNull()) null else columnValue
}

fun ResultSet.getDoubleOrNull(columnLabel: String): Double? {
    val columnValue: Double = getDouble(columnLabel)
    return if (wasNull()) null else columnValue
}

fun ResultSet.getDateOrNull(columnLabel: String): Date? {
    val columnValue: Date = getDate(columnLabel)
    return if (wasNull()) null else columnValue
}
