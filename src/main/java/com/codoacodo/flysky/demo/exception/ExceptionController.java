package com.codoacodo.flysky.demo.exception;

import com.codoacodo.flysky.demo.dto.response.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(VueloNotFoundException.class)
    public ResponseEntity<?> vueloNotFoundException(VueloNotFoundException e){
        ExceptionDto exceptionDto = new ExceptionDto(404, e.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }


}
