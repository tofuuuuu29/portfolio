package com.ralphmarondev.portfolio.mapper

import com.ralphmarondev.portfolio.dto.SkillResponse
import com.ralphmarondev.portfolio.entity.Skill

fun Skill.toResponse() = SkillResponse(id = id, name = name, category = category)
