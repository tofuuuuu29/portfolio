package com.ralphmarondev.portfolio.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun notFound(e: ResourceNotFoundException) = body(HttpStatus.NOT_FOUND, e.message)

    @ExceptionHandler(ResourceAlreadyExistsException::class)
    fun conflict(e: ResourceAlreadyExistsException) = body(HttpStatus.CONFLICT, e.message)

    @ExceptionHandler(InvalidCredentialsException::class)
    fun unauthorized(e: InvalidCredentialsException) = body(HttpStatus.UNAUTHORIZED, e.message)

    private fun body(status: HttpStatus, message: String?) =
        ResponseEntity.status(status).body(mapOf("detail" to (message ?: status.reasonPhrase)))
}
