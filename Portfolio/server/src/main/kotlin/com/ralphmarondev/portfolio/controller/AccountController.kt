package com.ralphmarondev.portfolio.controller

import com.ralphmarondev.portfolio.dto.AccountResponse
import com.ralphmarondev.portfolio.dto.LoginRequest
import com.ralphmarondev.portfolio.dto.LoginResponse
import com.ralphmarondev.portfolio.service.AccountService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("account/")
class AccountController(
    private val accountService: AccountService
) {
    @PostMapping("login/")
    fun login(@RequestBody request: LoginRequest): LoginResponse {
        return accountService.login(request)
    }

    @GetMapping("me/")
    fun me(authentication: Authentication): AccountResponse {
        return accountService.getById(authentication.principal as Long)
    }
}
