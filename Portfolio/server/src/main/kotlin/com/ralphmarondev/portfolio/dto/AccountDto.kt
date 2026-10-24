package com.ralphmarondev.portfolio.dto

data class LoginRequest(
    val username: String,
    val password: String
)

data class AccountResponse(
    val id: Long,
    val username: String,
    val role: String
)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val account: AccountResponse
)
