package com.mbi.study.common.exception;

import com.mbi.study.controller.dto.APIErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.Date;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AuthorizationTokenException.class})
    public ResponseEntity<APIErrorResponse> handleException(AuthorizationTokenException authorizationTokenException) {
        log.error(authorizationTokenException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new APIErrorResponse(authorizationTokenException.getMessage(), "002", Date.from(Instant.now())));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<APIErrorResponse> handleNotFoundExceptions(EntityNotFoundException validationException) {
        log.error(validationException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIErrorResponse(validationException.getMessage(), "003", Date.from(Instant.now())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIErrorResponse> handleValidationException(Exception validationException) {
        log.error(validationException.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIErrorResponse(validationException.getMessage(), "001", Date.from(Instant.now())));
    }
}
