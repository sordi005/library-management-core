package com.libraryManagement.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.libraryManagement.exception.repository.RepositoryException;
import com.libraryManagement.exception.response.ErrorResponse;
import com.libraryManagement.exception.service.ServiceException;
import com.libraryManagement.util.Logger; // 🔥 TU LOGGER PERSONALIZADO
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * MANEJO GLOBAL DE EXCEPCIONES
 *
 * Centraliza el manejo de todas las excepciones de la aplicación
 * y las convierte en respuestas HTTP apropiadas.
 */
public class GlobalExceptionHandler {

    // 🔧 CONFIGURACIÓN MEJORADA DE OBJECTMAPPER
    private static final ObjectMapper objectMapper = createConfiguredObjectMapper();

    /**
     * Crea ObjectMapper configurado para manejar fechas de Java 8+
     */
    private static ObjectMapper createConfiguredObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // REGISTRAR MÓDULO PARA JAVA TIME (LocalDateTime)
        mapper.registerModule(new JavaTimeModule());

        //  DESACTIVAR SERIALIZACIÓN DE FECHAS COMO TIMESTAMPS
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }

    /**
     * Maneja cualquier excepción y devuelve una respuesta HTTP apropiada
     */
    public static void handleException(HttpExchange exchange, Exception exception) throws IOException {
        ErrorResponse errorResponse;
        int statusCode;

        if (exception == null) {
            Logger.error("GlobalExceptionHandler: Excepción nula recibida");
            statusCode = 500;
            errorResponse = createErrorResponse("Error desconocido", statusCode, "unknown");
            sendErrorResponse(exchange, errorResponse, statusCode);
            return;
        }

        String requestPath = exchange != null ? exchange.getRequestURI().getPath() : "unknown";
        String requestMethod = exchange != null ? exchange.getRequestMethod() : "unknown";
        String context = requestMethod + " " + requestPath;

        // 🔧 MEJORA: Detectar y manejar causas raíz
        Throwable rootCause = getRootCause(exception);

        // 🔧 MEJORA: Manejar LazyInitializationException específicamente
        if (rootCause instanceof org.hibernate.LazyInitializationException) {
            Logger.error("LazyInitializationException detectada en " + context +
                ": " + rootCause.getMessage(), exception);

            statusCode = 500;
            errorResponse = createErrorResponse(
                "Error interno del servidor - datos no disponibles",
                statusCode,
                "lazy_loading"
            );
            sendErrorResponse(exchange, errorResponse, statusCode);
            return;
        }

        if (exception instanceof ServiceException) {
            ServiceException se = (ServiceException) exception;
            statusCode = mapServiceExceptionToHttpStatus(se);

            // 🛡️ MANEJAR NULLS EN ServiceException
            String message = se.getMessage() != null ? se.getMessage() : "Error de servicio sin mensaje";
            String operation = se.getOperation() != null ? se.getOperation() : "unknown";

            // 📝 LOG SEGÚN SEVERIDAD CON TU SISTEMA
            if (statusCode >= 500) {
                Logger.error("ServiceException [" + statusCode + "] en " + context +
                    ": " + message + " (Entity: " + se.getEntity() + ", Operation: " + operation + ")", exception);
            } else if (statusCode == 404) {
                Logger.warn("GlobalExceptionHandler", "Recurso no encontrado en " + context +
                    ": " + message + " (Entity: " + se.getEntity() + ", Operation: " + operation + ")");
            } else {
                Logger.info("GlobalExceptionHandler", "Error de validación en " + context +
                    ": " + message + " (Entity: " + se.getEntity() + ", Operation: " + operation + ")");
            }

            errorResponse = createErrorResponse(message, statusCode, operation);

        } else if (exception instanceof RepositoryException) {
            statusCode = 500;
            RepositoryException re = (RepositoryException) exception;

            Logger.error("RepositoryException en " + context +
                ": " + re.getMessage() + " (Entity: " + re.getEntity() + ", Operation: " + re.getOperation() + ")", re);

            errorResponse = createErrorResponse("Error en base de datos", statusCode, "database");

        } else if (exception instanceof IllegalArgumentException) {
            statusCode = 400;

            // 🛡️ MANEJAR NULL EN IllegalArgumentException
            String message = exception.getMessage() != null ?
                "Datos inválidos: " + exception.getMessage() :
                "Datos inválidos";

            Logger.warn("GlobalExceptionHandler", "Validación fallida en " + context + ": " + message);
            errorResponse = createErrorResponse(message, statusCode, "validation");

        } else {
            // 🔥 ERROR GENÉRICO
            statusCode = 500;

            // 🛡️ MANEJAR NULL EN EXCEPCIÓN GENÉRICA
            String message = exception.getMessage() != null ?
                exception.getMessage() :
                "Error interno del servidor";

            // 📝 LOG CRÍTICO PARA ERRORES NO MANEJADOS
            Logger.error("Error no manejado [" + exception.getClass().getSimpleName() + "] en " + context +
                ": " + message, exception);

            errorResponse = createErrorResponse(message, statusCode, "internal");
        }

        // 📊 LOG DE RESPUESTA (solo en DEBUG)
        Logger.debug("Enviando respuesta de error: Status=" + statusCode + ", Message=" + errorResponse.getMessage());

        sendErrorResponse(exchange, errorResponse, statusCode);
    }

    /**
     * 🔧 NUEVO: Obtiene la causa raíz de una excepción
     */
    private static Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }

    /**
     * Mapea ServiceException a códigos HTTP apropiados
     */
    private static int mapServiceExceptionToHttpStatus(ServiceException se) {
        // 🛡️ VALIDACIÓN DE NULL
        if (se == null || se.getMessage() == null) {
            return 500; // Error genérico si no hay información
        }

        String message = se.getMessage().toLowerCase();

        // 🔍 ERRORES 404 - RECURSOS NO ENCONTRADOS
        if (message.contains("no encontrado")) return 404;

        // ⚠️ ERRORES 409 - CONFLICTOS DE NEGOCIO
        if (message.contains("ya está registrado") || message.contains("duplicado")) return 409;
        if (message.contains("no se puede eliminar") || message.contains("préstamos activos")) return 409;

        // 📝 ERRORES 400 - DATOS INVÁLIDOS
        if (message.contains("inválido") || message.contains("requerido")) return 400;

        // 🔧 ERRORES 500 - ERRORES INTERNOS/TÉCNICOS
        if (message.contains("error inesperado") || message.contains("error interno")) return 500;

        return 500; // Error genérico para casos no contemplados
    }

    /**
     * Crea la estructura estándar de respuesta de error
     */
    private static ErrorResponse createErrorResponse(String message, int status, String operation) {
        // 🛡️ VALIDACIONES DE NULL
        String safeMessage = message != null ? message : "Error sin descripción";
        String safeOperation = operation != null ? operation : "unknown";

        return ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(status)
            .error(getHttpStatusText(status))
            .message(safeMessage)
            .operation(safeOperation)
            .build();
    }

    /**
     * Envía la respuesta de error al cliente
     */
    private static void sendErrorResponse(HttpExchange exchange, ErrorResponse errorResponse, int statusCode) throws IOException {
        //  VALIDACIÓN DE NULL
        if (exchange == null) {
            Logger.error("GlobalExceptionHandler: HttpExchange es null - No se puede enviar respuesta"); // CORREGIDO
            return;
        }

        if (errorResponse == null) {
            Logger.warn("GlobalExceptionHandler", "ErrorResponse es null, creando respuesta de emergencia");
            // Crear respuesta de emergencia
            errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(500)
                .error("Internal Server Error")
                .message("Error al crear respuesta de error")
                .operation("emergency")
                .build();
        }

        try {
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);

            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, responseBytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }

            // LOG EXITOSO
            Logger.debug("Respuesta de error enviada exitosamente: " + statusCode);

        } catch (Exception e) {
            //MANEJO DE ERROR AL ENVIAR RESPUESTA
            Logger.error("Error crítico al enviar respuesta de error", e);

            // Intentar enviar respuesta básica
            try {
                String emergencyResponse = "{\"error\":true,\"message\":\"Error crítico del servidor\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                byte[] emergencyBytes = emergencyResponse.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(500, emergencyBytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(emergencyBytes);
                }

                Logger.warn("GlobalExceptionHandler", "Respuesta de emergencia enviada");

            } catch (Exception fallbackError) {
                Logger.error("Error catastrófico - No se puede enviar respuesta", fallbackError);
            }
        }
    }
    /**
     * Convierte código HTTP a texto descriptivo
     */
    private static String getHttpStatusText(int status) {
        return switch (status) {
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 409 -> "Conflict";
            case 500 -> "Internal Server Error";
            default -> "Error";
        };
    }
}
