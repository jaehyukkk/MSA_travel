package com.example.travel_search.exception

enum class ErrorCode(var status: Int, var errorCode: String, var message: String) {
    UNAUTHORIZED(401, "UNAUTHORIZED-ERR-401", "자격 증명에 실패하였습니다."),
    FORBIDDEN(403, "FORBIDDEN-ERR-403", "해당 요청에 대한 권한이 없습니다."),
    ENTITY_NOT_FOUND(404, "ENTITY-NOT-FOUND-ERR-404", "존재하지 않는 데이터입니다."),
    BAD_REQUEST(400, "BAD_REQUEST-400", "잘못된 요청입니다"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL-SERVER-ERROR-500", "예상치 못한 오류가 발생하였습니다."),
}
