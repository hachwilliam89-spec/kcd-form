package com.kcdformes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO<T> {
    private int status;
    private String message;
    private T data;
    private String error;
    private LocalDateTime timestamp;

    private ApiResponseDTO(int status, String message, T data, String error) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }

    // Factories succès
    public static <T> ApiResponseDTO<T> created(String message, T data) {
        return new ApiResponseDTO<>(201, message, data, null);
    }

    public static <T> ApiResponseDTO<T> ok(String message, T data) {
        return new ApiResponseDTO<>(200, message, data, null);
    }

    public static ApiResponseDTO<Void> noContent(String message) {
        return new ApiResponseDTO<>(204, message, null, null);
    }

    // Factories erreurs
    public static ApiResponseDTO<Void> badRequest(String error) {
        return new ApiResponseDTO<>(400, null, null, error);
    }

    public static ApiResponseDTO<Void> notFound(String error) {
        return new ApiResponseDTO<>(404, null, null, error);
    }

    public static ApiResponseDTO<Void> conflict(String error) {
        return new ApiResponseDTO<>(409, null, null, error);
    }

    public static ApiResponseDTO<Void> unprocessable(String error) {
        return new ApiResponseDTO<>(422, null, null, error);
    }

    // Getters
    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public String getError() { return error; }
    public LocalDateTime getTimestamp() { return timestamp; }
}