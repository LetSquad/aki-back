package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import moscow.createdin.backend.model.dto.AreaDTO
import moscow.createdin.backend.model.dto.AreaDTOList
import moscow.createdin.backend.model.dto.BanRequestDTO
import moscow.createdin.backend.model.dto.CreateAreaRequestDTO
import moscow.createdin.backend.model.dto.EditAreaRequestDTO
import moscow.createdin.backend.service.AreaService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Методы работы с кластерами")
@RestController
@RequestMapping("/api/area")
class AreaController(private val areaService: AreaService) {

    @Operation(
        summary = "Создание кластера"
    )
    @PreAuthorize("hasRole('LANDLORD')")
    @PostMapping("create")
    fun create(createAreaReq: CreateAreaRequestDTO): AreaDTO {
        return areaService.create(createAreaReq)
    }

    @Operation(
        summary = "Изменение кластера"
    )
    @PreAuthorize("hasRole('LANDLORD')")
    @PutMapping("edit")
    fun edit(@RequestBody editAreaReq: EditAreaRequestDTO) {
        return areaService.update(editAreaReq)
    }

    @Operation(
        summary = "Получение кластера по его идентификатору"
    )
    @GetMapping("{id}")
    fun get(@Parameter(description = "ID кластера") @PathVariable id: Long): AreaDTO {
        return areaService.get(id)
    }

    @Operation(
        summary = "Получение всех кластеров"
    )
    @PreAuthorize("hasRole('LANDLORD')")
    @GetMapping
    fun get(): AreaDTOList {
        return areaService.get()
    }

    @Operation(
        summary = "Получение кластеров пользователя"
    )
    @GetMapping("user")
    fun getByUser(@Parameter(description = "ID пользователя") @RequestParam userId: Long): AreaDTOList {
        return areaService.getByUser(userId)
    }

    @Operation(
        summary = "Подтверждение кластера администратором"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("verify")
    fun verify(@RequestBody areaId: Long) {
        areaService.verify(areaId)
    }

    @Operation(
        summary = "Бан кластера администратором"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("ban")
    fun ban(@RequestBody banRequestDTO: BanRequestDTO) {
        areaService.ban(banRequestDTO)
    }

    @Operation(
        summary = "Удаление кластера"
    )
    @PreAuthorize("hasRole('LANDLORD')")
    @DeleteMapping
    fun delete(@RequestBody areaId: Long) {
        areaService.delete(areaId)
    }
}
