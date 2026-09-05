package com.vannguyen.java_learn_ecom.common.exception;

import com.vannguyen.java_learn_ecom.common.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        Map<String, Object> errors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        log.warn("Validation failed: errors={}", errors);

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(400, "Validation failed", errors));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException exception
    ) {
        log.warn("Business exception occurred: message={}", exception.getMessage());

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(400, exception.getMessage()));
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException exception
    ) {
        log.warn("Resource not found: message={}", exception.getMessage());

        return ResponseEntity
                .status(404)
                .body(ApiResponse.error(404, exception.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(
            DataIntegrityViolationException exception
    ) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("databaseMessage", exception.getMostSpecificCause().getMessage());

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(
                        400,
                        "Data violates database constraint",
                        details
                ));
    }
}