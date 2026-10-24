package com.ralphmarondev.portfolio.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "projects")
data class Project(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val title: String,
    @Column(columnDefinition = "TEXT")
    val description: String,
    @Column(name = "tech_stack")
    val techStack: String = "",
    @Column(name = "repo_url")
    val repoUrl: String? = null,
    @Column(name = "live_url")
    val liveUrl: String? = null,
    @Column(name = "image_url")
    val imageUrl: String? = null,
    val featured: Boolean = false,
    @Column(name = "is_deleted")
    val isDeleted: Boolean = false,
    @Column(name = "create_date")
    val createDate: LocalDateTime = LocalDateTime.now(),
    @Column(name = "update_date")
    val updateDate: LocalDateTime? = null
)
