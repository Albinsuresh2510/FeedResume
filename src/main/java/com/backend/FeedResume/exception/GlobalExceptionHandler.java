package com.backend.FeedResume.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<?> handleOtpExpired(OtpExpiredException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                    "error", "OTP_EXPIRED",
                    "message", ex.getMessage()
                ));
    }

    // Optional: log anything else too
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> catchAll(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "SERVER_ERROR",
                    "message", ex.getMessage()
                ));
    }
}