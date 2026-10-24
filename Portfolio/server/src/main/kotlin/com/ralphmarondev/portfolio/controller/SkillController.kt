package com.ralphmarondev.portfolio.controller

import com.ralphmarondev.portfolio.dto.SkillRequest
import com.ralphmarondev.portfolio.dto.SkillResponse
import com.ralphmarondev.portfolio.service.SkillService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("skill/")
class SkillController(
    private val skillService: SkillService
) {
    @GetMapping
    fun getAll(): List<SkillResponse> = skillService.getAll()

    @PostMapping
    fun create(@RequestBody request: SkillRequest): ResponseEntity<SkillResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.create(request))
    }

    @PutMapping("{id}/")
    fun update(@PathVariable id: Long, @RequestBody request: SkillRequest): SkillResponse {
        return skillService.update(id, request)
    }

    @DeleteMapping("{id}/")
    fun delete(@PathVariable id: Long): SkillResponse = skillService.delete(id)

    @PostMapping("batch/")
    fun batch(@RequestBody requests: List<SkillRequest>): ResponseEntity<List<SkillResponse>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.batch(requests))
    }
}
