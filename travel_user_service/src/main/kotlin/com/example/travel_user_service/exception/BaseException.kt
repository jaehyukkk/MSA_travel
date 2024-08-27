package com.example.travel_user_service.exception

class BaseException(val errorCode: ErrorCode) : RuntimeException(errorCode.message){
}
