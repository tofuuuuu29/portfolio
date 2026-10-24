package com.ralphmarondev.portfolio.dto

import java.time.LocalDateTime

data class ProjectRequest(
    val title: String,
    val description: String,
    val techStack: String = "",
    val repoUrl: String? = null,
    val liveUrl: String? = null,
    val imageUrl: String? = null,
    val featured: Boolean = false
)

data class ProjectResponse(
    val id: Long,
    val title: String,
    val description: String,
    val techStack: String,
    val repoUrl: String? = null,
    val liveUrl: String? = null,
    val imageUrl: String? = null,
    val featured: Boolean,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime? = null
)
