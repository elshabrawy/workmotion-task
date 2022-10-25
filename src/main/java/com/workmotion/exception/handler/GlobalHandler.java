package com.workmotion.exception.handler;

import com.workmotion.exception.custom.NotFoundData;
import com.workmotion.exception.custom.NotValidState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@ControllerAdvice
public class GlobalHandler {
    @ExceptionHandler(NotValidState.class)
    @ResponseBody
    public Object handleNotValidStateException(NotValidState notValidState, HttpServletRequest httpServletRequest){
       return new ResponseEntity<Object>(org.springframework.http.ResponseEntity.of(Optional.of(notValidState.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundData.class)
    @ResponseBody
    public Object handleNotFoundDataException(NotFoundData notFoundData, HttpServletRequest httpServletRequest){
        return new ResponseEntity<Object>(org.springframework.http.ResponseEntity.of(Optional.of(notFoundData.getMessage())), HttpStatus.NOT_FOUND);
    }
}
