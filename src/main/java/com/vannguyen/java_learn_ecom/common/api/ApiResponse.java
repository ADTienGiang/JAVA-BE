package com.vannguyen.java_learn_ecom.common.api;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiResponse<T>(
        int status,
        String message,
        T data,
        Map<String, Object> details,
        LocalDateTime timestamp
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                200,
                "Success",
                data,
                Map.of(),
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(
                201,
                "Created",
                data,
                Map.of(),
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(
                status,
                message,
                null,
                Map.of(),
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> error(int status, String message, Map<String, Object> details) {
        return new ApiResponse<>(
                status,
                message,
                null,
                details,
                LocalDateTime.now()
        );
    }
}