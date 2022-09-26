package com.registeration.demo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String,String> exceptionResponse(CustomException ex){
        Map<String, String> errorsMap = new HashMap<>();
        errorsMap.put(ex.getMessage(), ex.getHttpStatus().toString());
        return errorsMap;
    }
}
