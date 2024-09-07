package com.toyproject.project.global.exception;

import com.toyproject.project.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ApiResponse<?> handleCustomException(CustomException e) {
        return ApiResponse.fail(e.getErrorCode().getStatus(), e.getErrorCode().getMessage()).getBody();
    }

    // CustomException 못잡을 경우
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> exception(Exception e) {
        log.info("Exception : {}", e.getMessage());
        return ApiResponse.fail(500, e.getMessage()).getBody();
    }

}