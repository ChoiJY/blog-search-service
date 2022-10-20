package io.github.choijy.statisticsservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import io.github.choijy.statisticsservice.domain.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Description : 공통 Exception 처리 controller advice class. Created by jychoi on 2022/09/18.
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    /**
     * RuntimeException 기반 Exception 처리.
     *
     * @param exception RuntimeException 기반 Exception
     * @return ErrorResponse
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeExceptionHandler(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    /**
     * NoHandlerFoundException 기반 Exception 처리.
     *
     * @param exception RuntimeException 기반 Exception
     * @return ErrorResponse
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> noHandlerExceptionHandler(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(new ErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    /**
     * 미 처리 Exception 처리.
     *
     * @param exception Exception
     * @return ErrorResponse
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> uncaughtExceptionHandler(Exception exception) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()));
    }
}
