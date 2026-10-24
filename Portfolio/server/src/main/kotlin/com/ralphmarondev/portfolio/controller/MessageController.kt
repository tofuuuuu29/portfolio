package com.ralphmarondev.portfolio.controller

import com.ralphmarondev.portfolio.dto.MessageRequest
import com.ralphmarondev.portfolio.dto.MessageResponse
import com.ralphmarondev.portfolio.service.MessageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("message/")
class MessageController(
    private val messageService: MessageService
) {
    // Public: contact form on the portfolio
    @PostMapping
    fun create(@RequestBody request: MessageRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.create(request))
    }

    // Admin only
    @GetMapping
    fun getAll(): List<MessageResponse> = messageService.getAll()

    @DeleteMapping("{id}/")
    fun delete(@PathVariable id: Long): MessageResponse = messageService.delete(id)
}
