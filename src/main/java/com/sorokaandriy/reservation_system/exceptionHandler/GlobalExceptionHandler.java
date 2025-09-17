package com.sorokaandriy.reservation_system.exceptionHandler;

import com.sorokaandriy.reservation_system.controller.ReservationController;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e){
        log.error("Handle exception: called handleGenericException()" + e);

        return  ResponseEntity
                .status(500)
                .body(e.getMessage());
    }

    @ExceptionHandler(exception = {NoSuchElementException.class,
                                    EntityNotFoundException.class})
    public ResponseEntity<String> handleNoSuchEntityException(Exception e){
        log.error("Handle exception: called handleNoSuchEntityException" + e);

        return ResponseEntity
                .status(404)
                .body(e.getMessage());
    }


    @ExceptionHandler(exception = {IllegalArgumentException.class,
            IllegalStateException.class})
    public ResponseEntity<String> handleIllegalArgumentException(Exception e){
        log.error("Handle exception: called handleIllegalArgumentException" + e);

        return ResponseEntity
                .status(404)
                .body(e.getMessage());
    }
}
