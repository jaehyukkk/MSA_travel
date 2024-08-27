package com.example.travel_user_service.exception

enum class ErrorCode(var status: Int, var errorCode: String, var message: String) {
    UNAUTHORIZED(401, "UNAUTHORIZED-ERR-401", "자격 증명에 실패하였습니다."),
    FORBIDDEN(403, "FORBIDDEN-ERR-403", "해당 요청에 대한 권한이 없습니다."),
    ENTITY_NOT_FOUND(404, "ENTITY-NOT-FOUND-ERR-404", "존재하지 않는 데이터입니다."),
    BAD_REQUEST(400, "BAD_REQUEST-400", "잘못된 요청입니다"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL-SERVER-ERROR-500", "예상치 못한 오류가 발생하였습니다."),
    INVALID_REFRESH_TOKEN(401, "INVALID-REFRESH-TOKEN-ERR-401", "유효하지 않은 리프레시 토큰입니다."),
    LOGOUT_USER(401, "LOGOUT-USER-ERR-401", "로그아웃된 사용자입니다."),
    NOT_MATCH_USER(401, "NOT-MATCH-USER-ERR-401", "일치하는 사용자가 없습니다.")
}
