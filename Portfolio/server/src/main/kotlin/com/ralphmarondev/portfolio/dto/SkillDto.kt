package com.ralphmarondev.portfolio.dto

data class SkillRequest(
    val name: String,
    val category: String = "General"
)

data class SkillResponse(
    val id: Long,
    val name: String,
    val category: String
)
