package com.example.insta.validations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseErrorValidation {

    public ResponseEntity<Object> mapValidationService(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

//            if (!CollectionUtils.isEmpty(bindingResult.getAllErrors())) {
//                for (ObjectError error: bindingResult.getAllErrors()) {
//                    errors.put(error.getCode(), error.getDefaultMessage());
//                }
//            }

            if (!CollectionUtils.isEmpty(bindingResult.getAllErrors())) {
                for (ObjectError error : bindingResult.getAllErrors()) {
                    if (!bindingResult.getFieldErrors().contains(error)) {
                        errors.put(error.getCode(), error.getDefaultMessage());
                    }
                }
            }

            for (FieldError error: bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

//    public ResponseEntity<Object> mapValidationService(BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            Map<String, Map<String, String>> errors = new HashMap<>();
//
//            if (!CollectionUtils.isEmpty(bindingResult.getAllErrors())) {
//                for (ObjectError error: bindingResult.getAllErrors()) {
//                    Map<String, String> errorMap = new HashMap<>();
//
//                    errors.put(error.g, errorMap);
//                }
//            }
//
//            for (FieldError error: bindingResult.getFieldErrors()) {
//                errors.put(error.getField(), error.getDefaultMessage());
//            }
//            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//        }
//        return null;
//    }

}
