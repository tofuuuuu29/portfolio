package com.ralphmarondev.portfolio.service

import com.ralphmarondev.portfolio.dto.SkillRequest
import com.ralphmarondev.portfolio.dto.SkillResponse
import com.ralphmarondev.portfolio.entity.Skill
import com.ralphmarondev.portfolio.exception.ResourceAlreadyExistsException
import com.ralphmarondev.portfolio.exception.ResourceNotFoundException
import com.ralphmarondev.portfolio.mapper.toResponse
import com.ralphmarondev.portfolio.repository.SkillRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class SkillService(
    private val skillRepository: SkillRepository
) {
    fun getAll(): List<SkillResponse> {
        return skillRepository.findAll()
            .filter { !it.isDeleted }
            .map { it.toResponse() }
    }

    fun create(request: SkillRequest): SkillResponse {
        val exists = skillRepository.findAll()
            .any { !it.isDeleted && it.name.equals(request.name, ignoreCase = true) }
        if (exists) throw ResourceAlreadyExistsException("Skill '${request.name}' already exists.")

        return skillRepository.save(Skill(name = request.name, category = request.category)).toResponse()
    }

    fun update(id: Long, request: SkillRequest): SkillResponse {
        val updated = find(id).copy(
            name = request.name,
            category = request.category,
            updateDate = LocalDateTime.now()
        )
        return skillRepository.save(updated).toResponse()
    }

    fun delete(id: Long): SkillResponse {
        val deleted = find(id).copy(isDeleted = true, updateDate = LocalDateTime.now())
        return skillRepository.save(deleted).toResponse()
    }

    fun batch(requests: List<SkillRequest>): List<SkillResponse> = requests.map { create(it) }

    private fun find(id: Long): Skill {
        return skillRepository.findById(id)
            .filter { !it.isDeleted }
            .orElseThrow { ResourceNotFoundException("Skill not found.") }
    }
}
