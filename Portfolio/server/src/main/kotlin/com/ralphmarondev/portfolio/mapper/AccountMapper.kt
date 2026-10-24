package com.ralphmarondev.portfolio.mapper

import com.ralphmarondev.portfolio.dto.AccountResponse
import com.ralphmarondev.portfolio.entity.Account

fun Account.toResponse() = AccountResponse(id = id, username = username, role = role)
