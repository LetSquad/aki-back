package moscow.createdin.backend.controller

import io.swagger.v3.oas.annotations.Parameter
import moscow.createdin.backend.model.dto.AreaDTO
import moscow.createdin.backend.model.dto.AreaDTOList
import moscow.createdin.backend.model.dto.BanRequestDTO
import moscow.createdin.backend.model.dto.CreateAreaRequestDTO
import moscow.createdin.backend.model.dto.EditAreaRequestDTO
import moscow.createdin.backend.service.AreaService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/area")
class AreaController(private val areaService: AreaService) {

    @PreAuthorize("hasRole('LANDLORD')")
    @PostMapping("create")
    fun create(createAreaReq: CreateAreaRequestDTO): AreaDTO {
        return areaService.create(createAreaReq)
    }

    @PreAuthorize("hasRole('LANDLORD')")
    @PutMapping("edit")
    fun edit(@RequestBody editAreaReq: EditAreaRequestDTO) {
        return areaService.edit(editAreaReq)
    }

    @GetMapping("id")
    fun get(@Parameter(description = "ID кластера") @RequestParam id: Long): AreaDTO {
        return areaService.get(id)
    }

    @PreAuthorize("hasRole('LANDLORD')")
    @GetMapping
    fun get(): AreaDTOList {
        return areaService.get()
    }

    @GetMapping("user")
    fun getByUser(@Parameter(description = "ID пользователя") @RequestParam userId: Long): AreaDTOList {
        return areaService.getByUser(userId)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("verify")
    fun verify(@RequestBody areaId: Long) {
        areaService.verify(areaId)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("ban")
    fun ban(@RequestBody banRequestDTO: BanRequestDTO) {
        areaService.ban(banRequestDTO)
    }

    @PreAuthorize("hasRole('LANDLORD')")
    @DeleteMapping
    fun delete(@RequestBody areaId: Long) {
        areaService.delete(areaId)
    }
}
