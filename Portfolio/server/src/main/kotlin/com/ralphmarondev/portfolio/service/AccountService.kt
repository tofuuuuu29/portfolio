package com.ralphmarondev.portfolio.service

import com.ralphmarondev.portfolio.config.JwtService
import com.ralphmarondev.portfolio.dto.AccountResponse
import com.ralphmarondev.portfolio.dto.LoginRequest
import com.ralphmarondev.portfolio.dto.LoginResponse
import com.ralphmarondev.portfolio.exception.InvalidCredentialsException
import com.ralphmarondev.portfolio.exception.ResourceNotFoundException
import com.ralphmarondev.portfolio.mapper.toResponse
import com.ralphmarondev.portfolio.repository.AccountRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AccountService(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    fun login(request: LoginRequest): LoginResponse {
        val account = accountRepository.findByUsername(request.username)
            ?: throw InvalidCredentialsException("Invalid username or password.")

        if (!passwordEncoder.matches(request.password, account.password)) {
            throw InvalidCredentialsException("Invalid username or password.")
        }
        return LoginResponse(
            accessToken = jwtService.generateAccessToken(account.id, account.username, account.role),
            refreshToken = jwtService.generateRefreshToken(account.id, account.username),
            account = account.toResponse()
        )
    }

    fun getById(id: Long): AccountResponse {
        return accountRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Account not found.") }
            .toResponse()
    }
}
