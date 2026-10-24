package com.ralphmarondev.portfolio.entity

import com.ralphmarondev.portfolio.config.RoleConstants
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "accounts")
data class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(unique = true)
    val username: String,
    val password: String,
    val role: String = RoleConstants.ADMIN,
    @Column(name = "create_date")
    val createDate: LocalDateTime = LocalDateTime.now()
)
