package com.ralphmarondev.portfolio.dto

import java.time.LocalDateTime

data class MessageRequest(
    val name: String,
    val email: String,
    val content: String
)

data class MessageResponse(
    val id: Long,
    val name: String,
    val email: String,
    val content: String,
    val createDate: LocalDateTime
)
