package com.kcdformes.config;

import com.kcdformes.dto.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleResponseStatus(ResponseStatusException ex) {
        int status = ex.getStatusCode().value();
        String message = ex.getReason() != null ? ex.getReason() : "Erreur inconnue";

        ApiResponseDTO<Void> response = switch (status) {
            case 400 -> ApiResponseDTO.badRequest(message);
            case 404 -> ApiResponseDTO.notFound(message);
            case 409 -> ApiResponseDTO.conflict(message);
            case 422 -> ApiResponseDTO.unprocessable(message);
            default -> ApiResponseDTO.badRequest(message);
        };

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(400)
                .body(ApiResponseDTO.badRequest(ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(409)
                .body(ApiResponseDTO.conflict(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleGeneric(Exception ex) {
        return ResponseEntity.status(500)
                .body(ApiResponseDTO.badRequest("Erreur interne du serveur : " + ex.getMessage()));
    }
}