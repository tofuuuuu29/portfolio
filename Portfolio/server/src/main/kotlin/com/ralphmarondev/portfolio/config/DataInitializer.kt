package com.ralphmarondev.portfolio.config

import com.ralphmarondev.portfolio.entity.Account
import com.ralphmarondev.portfolio.repository.AccountRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DataInitializer(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
) : CommandLineRunner {

    @Transactional
    override fun run(vararg args: String) {
        val username = System.getenv("ADMIN_USERNAME") ?: "admin"
        val password = System.getenv("ADMIN_PASSWORD")

        if (password.isNullOrBlank()) {
            println("ADMIN_PASSWORD not set; skipping admin creation.")
            return
        }
        if (accountRepository.findByUsername(username) != null) {
            println("Admin account already exists.")
            return
        }
        accountRepository.save(
            Account(username = username, password = passwordEncoder.encode(password)!!)
        )
        println("Admin account created: $username")
    }
}
