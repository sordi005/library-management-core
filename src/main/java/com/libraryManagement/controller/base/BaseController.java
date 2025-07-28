package com.libraryManagement.controller.base;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;

/**
 * 🎯 CONTROLADOR BASE - FUNDAMENTO DE TODA API HTTP
 * Funcionalidades principales:
 * - Manejo automático de requests/responses HTTP
 * - Serialización/deserialización JSON profesional
 * - Headers CORS para integración con frontends
 * - Manejo centralizado de errores
 * - Métodos utilitarios para controllers específicos
 */
public abstract class BaseController implements HttpHandler {

    // =====================================================================
    // CONFIGURACIÓN CORE - EL CEREBRO DEL CONTROLLER
    // =====================================================================

    /**
     * OBJECT MAPPER - El traductor universal Java ↔ JSON
     * Se configura UNA VEZ aquí y lo usan TODOS los controllers.
     */
    protected final ObjectMapper objectMapper;
    /**
     * CONSTRUCTOR - Configuración inicial
     *
     * Aquí se configura TODO lo necesario para manejar JSON de forma profesional.
     * Esta configuración se ejecuta UNA SOLA VEZ por controller.
     */
    protected BaseController() {
        // Crear el traductor JSON
        this.objectMapper = new ObjectMapper();

        // SOPORTE PARA FECHAS MODERNAS (LocalDate, LocalDateTime)
        this.objectMapper.registerModule(new JavaTimeModule());

        // DESACTIVAR SERIALIZACIÓN DE FECHAS COMO TIMESTAMPS
        this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // NO INCLUIR CAMPOS NULL EN JSON
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    // =====================================================================
    // MANEJO PRINCIPAL DE REQUESTS - EL PUNTO DE ENTRADA
    // =====================================================================

    /**
     * MÉTODO PRINCIPAL - Llegan TODAS las peticiones HTTP
     *
     * Este método es llamado automáticamente por el HttpServer de Java
     * cada vez que alguien hace una petición a tu API.
     *
     * FLUJO:
     * 1. Cliente hace petición → Este método se ejecuta
     * 2. Configura CORS (permite frontend)
     * 3. Maneja preflight requests (requerimiento del navegador)
     * 4. Delega al controller específico
     * 5. Si hay error, lo maneja automáticamente
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // Configurar CORS para permitir frontends
            setCorsHeaders(exchange);

            // Manejar preflight requests del navegador
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 200, "OK");
                return; // Termina aquí para requests OPTIONS
            }

            // 🎯 PASO 3: Delegar al controller específico
            // Este método es ABSTRACTO, cada controller lo implementa
            handleRequest(exchange);

        } catch (Exception e) {
            com.libraryManagement.exception.GlobalExceptionHandler.handleException(exchange, e);
        }
    }

    /**
     * MÉTODO ABSTRACTO - Cada controller implementa SU lógica
     */
    protected abstract void handleRequest(HttpExchange exchange) throws IOException;

    // =====================================================================
    // MÉTODOS DE RESPUESTA - COMUNICACIÓN CON EL CLIENTE
    // =====================================================================

    /**
     * ENVIAR RESPUESTA JSON - El método MÁS USADO
     *
     * Este método convierte automáticamente cualquier objeto Java a JSON
     * y lo envía al cliente con los headers correctos.
     */
    protected void sendJsonResponse(HttpExchange exchange, int statusCode, Object data) throws IOException {
        // PASO 1: Convertir objeto Java → JSON string
        String jsonResponse = objectMapper.writeValueAsString(data);

        // PASO 2: Enviar con Content-Type correcto
        sendResponse(exchange, statusCode, jsonResponse, "application/json");
    }

    /**
     * 📤 ENVIAR RESPUESTA DE TEXTO SIMPLE
     *
     * Para mensajes simples como "OK", "Not Found", etc.
     */
    protected void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        sendResponse(exchange, statusCode, response, "text/plain");
    }

    /**
     * 📤 MÉTODO MAESTRO DE RESPUESTA - Aquí pasa la magia HTTP
     *
     * Este es el método que REALMENTE envía datos al cliente.
     * Todos los demás métodos terminan llamando a este.
     *
     * PASOS DEL PROTOCOLO HTTP:
     * 1. Establecer Content-Type (dice qué tipo de dato envías)
     * 2. Enviar headers con código de estado (200, 404, 500, etc.)
     * 3. Escribir el contenido real (JSON, texto, etc.)
     */
    protected void sendResponse(HttpExchange exchange, int statusCode, String response, String contentType) throws IOException {
        // PASO 1: Establecer tipo de contenido
        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");

        // PASO 2: Enviar headers HTTP con código de estado y tamaño
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        // PASO 3: Escribir el contenido real
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    // =====================================================================
    // CONFIGURACIÓN CORS - INTEGRACIÓN CON FRONTENDS
    // =====================================================================

    /**
     * CONFIGURAR HEADERS CORS - Permite que frontends consuman tu API
     *
     * CORS (Cross-Origin Resource Sharing) es FUNDAMENTAL para APIs modernas.
     * Sin esto, ningún frontend podrá usar tu API.
     *
     * ESCENARIO TÍPICO:
     * - API corriendo en: http://localhost:8080
     * - frontend corriendo en: http://localhost:3000
     * - Sin CORS: El navegador BLOQUEA todas las peticiones
     * - Con CORS: Todo funciona perfectamente
     */
    private void setCorsHeaders(HttpExchange exchange) {
        var headers = exchange.getResponseHeaders();

        // Permitir peticiones desde cualquier origen (en desarrollo)
        // En producción cambiarías "*" por tu dominio específico
        headers.set("Access-Control-Allow-Origin", "*");

        // Métodos HTTP permitidos
        headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        // Headers que el cliente puede enviar
        headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    // =====================================================================
    // MANEJO DE ERRORES - RECUPERACIÓN PROFESIONAL
    // =====================================================================

    /**
     * MANEJO CENTRALIZADO DE ERRORES
     *
     * Cuando algo falla en la API, este método asegura que:
     * 1. El cliente reciba una respuesta útil (no un crash)
     * 2. El error se formatee de forma profesional
     * 3. Se incluya información de debugging
     */
    protected void handleError(HttpExchange exchange, Exception e) throws IOException {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", true);
        errorResponse.put("message", e.getMessage());
        errorResponse.put("timestamp", java.time.LocalDateTime.now());
        errorResponse.put("type", e.getClass().getSimpleName());

        sendJsonResponse(exchange, 500, errorResponse);
    }

    // =====================================================================
    // MÉTODOS UTILITARIOS - HERRAMIENTAS PARA CONTROLLERS
    // =====================================================================

    /**
     * LEER BODY DEL REQUEST
     *
     * Cuando el cliente envía datos (POST, PUT), este método
     * los convierte de bytes HTTP → String legible.
     */
    protected String getRequestBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * EXTRAER ID DE LA URL
     *
     * Convierte /api/users/123 → 123 (Long)
     * Útil para endpoints como GET /api/users/{id}
     */
    protected Long extractIdFromPath(String path, String basePath) {
        try {
            String idPart = path.substring(basePath.length());
            if (idPart.startsWith("/")) {
                idPart = idPart.substring(1);
            }
            return Long.parseLong(idPart);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 🔍 EXTRAER PARÁMETROS DE QUERY
     *
     * Convierte ?name=Juan&age=25 → Map{name: "Juan", age: "25"}
     * Útil para filtros y búsquedas
     */
    protected Map<String, String> getQueryParams(HttpExchange exchange) {
        Map<String, String> params = new HashMap<>();
        String query = exchange.getRequestURI().getQuery();

        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }

        return params;
    }
}
