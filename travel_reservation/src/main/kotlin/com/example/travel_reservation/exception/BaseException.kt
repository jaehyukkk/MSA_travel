package com.example.travel_reservation.exception

class BaseException(val errorCode: ErrorCode) : RuntimeException(errorCode.message){
}
