package com.app.exception.handler;

import com.app.exception.*;
import com.app.exception.model.ErrorMessage;
import com.app.exception.valid.ClientValidationException;
import com.app.exception.valid.OrderValidationException;
import com.app.exception.valid.RoomValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {ClientValidationException.class, RoomValidationException.class, OrderValidationException.class})
    public ResponseEntity<ErrorMessage> handleException(ApplicationGlobalException exception, HttpServletRequest request){
        var httpStatus = exception.getHttpStatus();

        log.error(String.format("Exception received, path:  '%s'", request.getRequestURI()), exception);

        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(exception.getMessage())
                .statusCode(httpStatus.value())
                .timestamp(System.currentTimeMillis())
                .error(exception.getClass().getName())
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
