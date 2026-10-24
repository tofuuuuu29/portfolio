package com.ralphmarondev.portfolio.service

import com.ralphmarondev.portfolio.dto.ProjectRequest
import com.ralphmarondev.portfolio.dto.ProjectResponse
import com.ralphmarondev.portfolio.entity.Project
import com.ralphmarondev.portfolio.exception.ResourceNotFoundException
import com.ralphmarondev.portfolio.mapper.toResponse
import com.ralphmarondev.portfolio.repository.ProjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class ProjectService(
    private val projectRepository: ProjectRepository
) {
    fun getAll(): List<ProjectResponse> {
        return projectRepository.findAll()
            .filter { !it.isDeleted }
            .sortedWith(compareByDescending<Project> { it.featured }.thenByDescending { it.createDate })
            .map { it.toResponse() }
    }

    fun getById(id: Long): ProjectResponse = find(id).toResponse()

    fun create(request: ProjectRequest): ProjectResponse {
        val project = Project(
            title = request.title,
            description = request.description,
            techStack = request.techStack,
            repoUrl = request.repoUrl,
            liveUrl = request.liveUrl,
            imageUrl = request.imageUrl,
            featured = request.featured
        )
        return projectRepository.save(project).toResponse()
    }

    fun update(id: Long, request: ProjectRequest): ProjectResponse {
        val updated = find(id).copy(
            title = request.title,
            description = request.description,
            techStack = request.techStack,
            repoUrl = request.repoUrl,
            liveUrl = request.liveUrl,
            imageUrl = request.imageUrl,
            featured = request.featured,
            updateDate = LocalDateTime.now()
        )
        return projectRepository.save(updated).toResponse()
    }

    fun delete(id: Long): ProjectResponse {
        val deleted = find(id).copy(isDeleted = true, updateDate = LocalDateTime.now())
        return projectRepository.save(deleted).toResponse()
    }

    fun batch(requests: List<ProjectRequest>): List<ProjectResponse> = requests.map { create(it) }

    private fun find(id: Long): Project {
        return projectRepository.findById(id)
            .filter { !it.isDeleted }
            .orElseThrow { ResourceNotFoundException("Project not found.") }
    }
}
