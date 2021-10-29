package com.example.insta.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserExistException.class)
    public final ResponseEntity<Object> handleUserExistException(UserExistException ex) {
        Map<String, String> errors = new HashMap<>();
        String exMessage = ex.getLocalizedMessage();
        errors.put("error", exMessage);
        ErrorResponse error = new ErrorResponse("User Exist", errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public final ResponseEntity<Object> handleAllExceptions(HttpServerErrorException.InternalServerError ex) {
        Map<String, String> errors = new HashMap<>();
        String exMessage = ex.getLocalizedMessage();
        errors.put("error", exMessage);
        ErrorResponse error = new ErrorResponse("Server Error", errors);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        String exMessage = ex.getLocalizedMessage();
        errors.put("error", exMessage);
        ErrorResponse error = new ErrorResponse("Record Not Found", errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (!ex.getBindingResult().getFieldErrors().contains(error)) {
                errors.put(error.getCode(), error.getDefaultMessage());
            }
        }

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ErrorResponse error = new ErrorResponse("Validation Failed", errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
