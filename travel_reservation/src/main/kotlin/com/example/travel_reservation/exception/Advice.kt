package com.example.travel_reservation.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class Advice {
    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(e.errorCode)
        return ResponseEntity(errorResponse, HttpStatus.valueOf(e.errorCode.status))
    }
}
