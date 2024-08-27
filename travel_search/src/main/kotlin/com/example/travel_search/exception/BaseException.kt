package com.example.travel_search.exception

class BaseException(val errorCode: ErrorCode) : RuntimeException(errorCode.message){
}
