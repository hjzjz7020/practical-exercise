package com.example.server.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author Jingze Zheng
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {
    @ExceptionHandler(value = InternalException.class)
    public ResponseEntity<Object> handleInternalException(InternalException ex) {
        return this.buildErrorResponseEntity(ex.getHttpStatus(), ex.getMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        return this.buildErrorResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        for (ObjectError error : errors) {
            errorMessage.append(((FieldError) error).getField()).append(" ").append(error.getDefaultMessage()).append(". ");
        }

        return this.buildErrorResponseEntity(HttpStatus.BAD_REQUEST, errorMessage.toString());
    }

    private ResponseEntity<Object> buildErrorResponseEntity(HttpStatus status, String message){
        Error error = new Error(status.toString(), message);
        return new ResponseEntity<>(error, status);
    }
}
