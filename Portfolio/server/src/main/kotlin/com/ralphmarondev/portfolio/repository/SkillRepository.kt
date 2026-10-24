package com.ralphmarondev.portfolio.repository

import com.ralphmarondev.portfolio.entity.Skill
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SkillRepository : JpaRepository<Skill, Long>
