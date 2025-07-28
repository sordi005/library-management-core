package com.libraryManagement.exception.response;

import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * RESPUESTA ESTÁNDAR DE ERROR
 *
 * Estructura unificada para todas las respuestas de error de la API
 */
@Data
@Builder
public class ErrorResponse {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private int status;
    private String error;
    private String message;
    private String operation;
    private String path;

    /**
     * Crear respuesta de error simple
     */
    public static ErrorResponse simple(String message, int status) {
        return ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(status)
            .error(getHttpStatusText(status))
            .message(message)
            .build();
    }

    /**
     * Crear respuesta de error completa
     */
    public static ErrorResponse complete(String message, int status, String operation, String path) {
        return ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(status)
            .error(getHttpStatusText(status))
            .message(message)
            .operation(operation)
            .path(path)
            .build();
    }

    /**
     * Convierte código HTTP a texto descriptivo
     */
    private static String getHttpStatusText(int status) {
        return switch (status) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 409 -> "Conflict";
            case 422 -> "Unprocessable Entity";
            case 500 -> "Internal Server Error";
            case 503 -> "Service Unavailable";
            default -> "Error";
        };
    }
}
