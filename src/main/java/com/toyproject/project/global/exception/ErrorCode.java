package com.toyproject.project.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 : 잘못된 요청
    INVALID_REQUEST_METHOD(400, "요청 메서드가 유효하지 않습니다"),
    TYPE_MISMATCH(400, "요청 데이터 중 유효하지 않은 타입이 있습니다."),
    AUTHOR_CANNOT_APPLY(400, "글쓴이는 신청할 수 없습니다."),


    // 401 : 접근 권한이 없음
    NO_AUTHORITY(401, "접근 권한이 없습니다."),
    TOKEN_EXPIRED(401, "토큰이 만료되었습니다."),

    // 403: Forbidden
    PASSWORD_ERROR(403, "비밀번호가 일치하지 않습니다."),

    // 404: 잘못된 리소스 접근
    NOT_FOUND_GATEWAY(404, "존재하지 않는 경로입니다."),
    NOT_FOUND_ENTITY(404, "해당 객체를 찾지 못했습니다."),
    NOT_FOUND_MEMBER(404, "존재하지 않는 사용자입니다."),
    NOT_FOUND_BOARD(404, "존재하지 않는 게시글입니다."),

    //409 : 중복된 리소스
    ALREADY_EXIST_MEMBER(409, "이미 존재하는 사용자입니다."),
    ALREADY_EXIST_PASSWORD(409, "이미 사용 중인 비밀번호입니다."),
    ALREADY_EXIST_PARTICIPANT(409, "이미 동행 신청한 게시글입니다."),

    //500 : INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버 내부 에러입니다.");

    private final Integer status;
    private final String message;

}
