package com.example.travel_search.exception

class ErrorResponse(
    var status: Int = 500,
    var message: String? = null,
    var code: String? = null
) {
    constructor(errorCode: ErrorCode) : this(errorCode.status, errorCode.message, errorCode.errorCode)
}
