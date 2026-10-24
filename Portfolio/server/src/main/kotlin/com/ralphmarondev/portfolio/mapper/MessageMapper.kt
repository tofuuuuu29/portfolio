package com.ralphmarondev.portfolio.mapper

import com.ralphmarondev.portfolio.dto.MessageResponse
import com.ralphmarondev.portfolio.entity.Message

fun Message.toResponse() = MessageResponse(
    id = id, name = name, email = email, content = content, createDate = createDate
)
