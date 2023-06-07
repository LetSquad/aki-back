package moscow.createdin.backend.repository.mapper

import org.postgresql.util.PGobject

fun String?.toPGObject(): PGobject {
    val pGobject = PGobject()
    pGobject.type = "jsonb"
    pGobject.value = this
    return pGobject
}
