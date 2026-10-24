package com.ralphmarondev.portfolio.mapper

import com.ralphmarondev.portfolio.dto.ProjectResponse
import com.ralphmarondev.portfolio.entity.Project

fun Project.toResponse() = ProjectResponse(
    id = id,
    title = title,
    description = description,
    techStack = techStack,
    repoUrl = repoUrl,
    liveUrl = liveUrl,
    imageUrl = imageUrl,
    featured = featured,
    createDate = createDate,
    updateDate = updateDate
)
