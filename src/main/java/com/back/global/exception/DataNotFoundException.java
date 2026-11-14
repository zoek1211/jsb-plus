package com.back.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// "찾는 데이터가 없다"는 뜻의 우리만의 예외 (교재 2-10, 14163 35강).
// Our own exception meaning "the requested data doesn't exist" (book 2-10, 14163 lecture 35).
//
// @ResponseStatus(NOT_FOUND): 이 예외가 컨트롤러 밖으로 던져지면 스프링이 HTTP 404 로 응답한다.
// 없으면 기본값인 500(서버 내부 오류)이 된다 → "서버가 고장났다"가 아니라 "없는 주소다"라고 정확히 알려준다.
// @ResponseStatus(NOT_FOUND): when this exception escapes a controller, Spring responds with HTTP 404.
// Without it, the default is 500 (internal server error) → this states precisely "no such page" rather than "the server broke".
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
// RuntimeException 을 상속: 메서드에 throws 를 적지 않아도 던질 수 있는 예외 (unchecked exception).
// Extends RuntimeException: an unchecked exception, so methods can throw it without declaring "throws".
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
