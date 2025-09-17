package com.sorokaandriy.reservation_system.exceptionHandler;

import com.sorokaandriy.reservation_system.controller.ReservationController;
import com.sorokaandriy.reservation_system.dto.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception e){
        log.error("Handle exception: called handleException()" + e);

        var errorResponseDto = new ErrorResponseDto(
                "Internal sever error",
                        e.getMessage(),
                        LocalDateTime.now()
        );
        return  ResponseEntity
                .status(404)
                .body(errorResponseDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoSuchEntityException(Exception e){
        log.error("Handle exception: called handleNoSuchEntityException" + e);

        var errorResponseDto = new ErrorResponseDto(
                "Entity not found",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(404)
                .body(errorResponseDto);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(Exception e){
        log.error("Handle exception: called handleIllegalArgumentException" + e);

        var errorResponseDto = new ErrorResponseDto(
                "Invalid arguments passed",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(404)
                .body(errorResponseDto);
    }
}
