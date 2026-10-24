package com.ralphmarondev.portfolio.controller

import com.ralphmarondev.portfolio.dto.ProjectRequest
import com.ralphmarondev.portfolio.dto.ProjectResponse
import com.ralphmarondev.portfolio.service.ProjectService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("project/")
class ProjectController(
    private val projectService: ProjectService
) {
    @GetMapping
    fun getAll(): List<ProjectResponse> = projectService.getAll()

    @GetMapping("{id}/")
    fun getById(@PathVariable id: Long): ProjectResponse = projectService.getById(id)

    @PostMapping
    fun create(@RequestBody request: ProjectRequest): ResponseEntity<ProjectResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(request))
    }

    @PutMapping("{id}/")
    fun update(@PathVariable id: Long, @RequestBody request: ProjectRequest): ProjectResponse {
        return projectService.update(id, request)
    }

    @DeleteMapping("{id}/")
    fun delete(@PathVariable id: Long): ProjectResponse = projectService.delete(id)

    @PostMapping("batch/")
    fun batch(@RequestBody requests: List<ProjectRequest>): ResponseEntity<List<ProjectResponse>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.batch(requests))
    }
}
