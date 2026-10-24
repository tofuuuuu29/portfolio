package com.ralphmarondev.portfolio.repository

import com.ralphmarondev.portfolio.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long>
