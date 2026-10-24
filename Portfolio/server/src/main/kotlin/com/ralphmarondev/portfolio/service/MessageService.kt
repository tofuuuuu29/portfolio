package com.ralphmarondev.portfolio.service

import com.ralphmarondev.portfolio.dto.MessageRequest
import com.ralphmarondev.portfolio.dto.MessageResponse
import com.ralphmarondev.portfolio.entity.Message
import com.ralphmarondev.portfolio.exception.ResourceNotFoundException
import com.ralphmarondev.portfolio.mapper.toResponse
import com.ralphmarondev.portfolio.repository.MessageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MessageService(
    private val messageRepository: MessageRepository
) {
    fun getAll(): List<MessageResponse> {
        return messageRepository.findAll()
            .filter { !it.isDeleted }
            .sortedByDescending { it.createDate }
            .map { it.toResponse() }
    }

    fun create(request: MessageRequest): MessageResponse {
        val message = Message(name = request.name, email = request.email, content = request.content)
        return messageRepository.save(message).toResponse()
    }

    fun delete(id: Long): MessageResponse {
        val message = messageRepository.findById(id)
            .filter { !it.isDeleted }
            .orElseThrow { ResourceNotFoundException("Message not found.") }
        return messageRepository.save(message.copy(isDeleted = true)).toResponse()
    }
}
