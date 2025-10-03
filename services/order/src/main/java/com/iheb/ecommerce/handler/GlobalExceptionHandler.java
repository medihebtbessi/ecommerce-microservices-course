package com.iheb.ecommerce.handler;

import com.iheb.ecommerce.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handle(BusinessException exp) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exp.getMessage());
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handle(EntityNotFoundException exp) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exp.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exp) {

        var errors = new HashMap<String, String>();
        exp.getBindingResult().getAllErrors().forEach((err) -> {
            var fieldName=((FieldError) err ).getField();
            var errorMsg=err.getDefaultMessage();
            errors.put(fieldName,errorMsg);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errors));
    }
}
