package com.libraryManagement.controller;

import com.libraryManagement.controller.base.BaseController;
import com.libraryManagement.dto.UserDTO.CreateUserDTO;
import com.libraryManagement.dto.UserDTO.UpdateUserDTO;
import com.libraryManagement.dto.UserDTO.UserDetailDTO;
import com.libraryManagement.dto.UserDTO.UserSimpleDTO;
import com.libraryManagement.exception.GlobalExceptionHandler;
import com.libraryManagement.service.interfaces.UserService;
import com.libraryManagement.util.Logger; //Sistema de logging personalizado
import com.libraryManagement.validation.DTOValidator;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

/**
 * USER CONTROLLER
 *
 * Endpoints disponibles:
 * GET    /api/users           - Listar todos los usuarios
 * GET    /api/users/{id}      - Obtener usuario por ID
 * POST   /api/users           - Crear nuevo usuario
 * PUT    /api/users/{id}      - Actualizar usuario
 * DELETE /api/users/{id}      - Eliminar usuario
 */
public class UserController extends BaseController {

    private final UserService userService;
    private static final String BASE_PATH = "/api/users";

    public UserController(UserService userService) {
        super();
        this.userService = userService;
        Logger.info("UserController", "Inicializado correctamente");
    }

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        Logger.info("UserController", method + " " + path + " - Request recibido");

        try {
            switch (method.toUpperCase()) {
                case "GET":
                    handleGetRequest(exchange, path);
                    break;
                case "POST":
                    handlePostRequest(exchange, path);
                    break;
                case "PUT":
                    handlePutRequest(exchange, path);
                    break;
                case "DELETE":
                    handleDeleteRequest(exchange, path);
                    break;
                default:
                    sendResponse(exchange, 405, "Method Not Allowed");
            }
        } catch (Exception e) {
            Logger.error("UserController", "Error no manejado en " + method + " " + path, e);
            GlobalExceptionHandler.handleException(exchange, e);
        }
    }

    /**
     * MANEJO DE REQUESTS GET
     */
    private void handleGetRequest(HttpExchange exchange, String path) throws IOException {
        if (path.equals(BASE_PATH)) {
            // GET /api/users - Listar todos
            handleGetAllUsers(exchange);
        } else if (path.startsWith(BASE_PATH + "/")) {
            // GET /api/users/{id} - Obtener por ID
            handleGetUserById(exchange, path);
        } else {
            sendResponse(exchange, 404, "Endpoint not found");
        }
    }

    /**
     * MANEJO DE REQUESTS POST
     */
    private void handlePostRequest(HttpExchange exchange, String path) throws IOException {
        if (path.equals(BASE_PATH)) {
            // POST /api/users - Crear usuario
            handleCreateUser(exchange);
        } else {
            sendResponse(exchange, 404, "Endpoint not found");
        }
    }

    /**
     * MANEJO DE REQUESTS PUT
     */
    private void handlePutRequest(HttpExchange exchange, String path) throws IOException {
        if (path.startsWith(BASE_PATH + "/")) {
            // PUT /api/users/{id} - Actualizar usuario
            handleUpdateUser(exchange, path);
        } else {
            sendResponse(exchange, 404, "Endpoint not found");
        }
    }

    /**
     * MANEJO DE REQUESTS DELETE
     */
    private void handleDeleteRequest(HttpExchange exchange, String path) throws IOException {
        if (path.startsWith(BASE_PATH + "/")) {
            // DELETE /api/users/{id} - Eliminar usuario
            handleDeleteUser(exchange, path);
        } else {
            sendResponse(exchange, 404, "Endpoint not found");
        }
    }

    // =====================================================================
    // IMPLEMENTACIÓN DE ENDPOINTS ESPECÍFICOS
    // =====================================================================

    /**
     *  GET /api/users - Listar todos los usuarios
     */
    private void handleGetAllUsers(HttpExchange exchange) throws IOException {
        try {
            // 📝 LOG: Solo el request (sin startOperation duplicado)
            Logger.debug("UserController", "Procesando GET /api/users");

            List<UserSimpleDTO> users = userService.findAllUsers();

            // 📝 LOG: Solo el resultado final
            Logger.info("UserController", "GET /api/users completado - " + users.size() + " usuarios devueltos");

            sendJsonResponse(exchange, 200, users);

        } catch (Exception e) {
            Logger.error("UserController", "Error en GET /api/users", e);
            GlobalExceptionHandler.handleException(exchange, e);
        }
    }

    /**
     *  GET /api/users/{id} - Obtener usuario por ID
     */
    private void handleGetUserById(HttpExchange exchange, String path) throws IOException {
        Long userId = extractIdFromPath(path, BASE_PATH);

        if (userId == null) {
            Logger.warn("UserController", "ID inválido en GET request: " + path);
            sendJsonResponse(exchange, 400, Map.of(
                "error", "Bad Request",
                "message", "Invalid user ID format",
                "timestamp", LocalDateTime.now()
            ));
            return;
        }

        try {
            Logger.debug("UserController", "Procesando GET usuario ID: " + userId);

            UserDetailDTO user = userService.findUserById(userId);

            Logger.info("UserController", "GET /api/users/" + userId + " completado - Usuario: " + user.getEmail());
            sendJsonResponse(exchange, 200, user);

        } catch (Exception e) {
            Logger.error("UserController", "Error obteniendo usuario " + userId, e);
            GlobalExceptionHandler.handleException(exchange, e);
        }
    }

    /**
     * POST /api/users - Crear nuevo usuario
     */
    private void handleCreateUser(HttpExchange exchange) throws IOException {
        try {
            String body = getRequestBody(exchange);
            Logger.debug("UserController", "Procesando POST /api/users");

            CreateUserDTO createDTO = objectMapper.readValue(body, CreateUserDTO.class);
            DTOValidator.validate(createDTO);

            UserDetailDTO user = userService.createUser(createDTO);

            Logger.info("UserController", "POST /api/users completado - Usuario creado: " + user.getEmail() + " (ID: " + user.getId() + ")");
            sendJsonResponse(exchange, 201, user);

        } catch (Exception e) {
            Logger.error("UserController", "Error en POST /api/users", e);
            GlobalExceptionHandler.handleException(exchange, e);
        }
    }

    /**
     *  PUT /api/users/{id} - Actualizar usuario
     */
    private void handleUpdateUser(HttpExchange exchange, String path) throws IOException {
        Long userId = extractIdFromPath(path, BASE_PATH);

        if (userId == null) {
            Logger.warn("UserController", "ID inválido en PUT request: " + path);
            sendJsonResponse(exchange, 400, Map.of(
                "error", "Bad Request",
                "message", "Invalid user ID format",
                "timestamp", LocalDateTime.now()
            ));
            return;
        }

        try {
            String body = getRequestBody(exchange);
            UpdateUserDTO updateDTO = objectMapper.readValue(body, UpdateUserDTO.class);
            DTOValidator.validate(updateDTO);

            Logger.debug("UserController", "Procesando PUT usuario ID: " + userId);

            UserDetailDTO user = userService.updateUser(userId, updateDTO);

            Logger.info("UserController", "PUT /api/users/" + userId + " completado - Usuario actualizado: " + user.getEmail());
            sendJsonResponse(exchange, 200, user);

        } catch (Exception e) {
            Logger.error("UserController", "Error actualizando usuario " + userId, e);
            GlobalExceptionHandler.handleException(exchange, e);
        }
    }

    /**
     *  DELETE /api/users/{id} - Eliminar usuario
     */
    private void handleDeleteUser(HttpExchange exchange, String path) throws IOException {
        Long userId = extractIdFromPath(path, BASE_PATH);

        if (userId == null) {
            // LOG: Solo para debugging interno
            Logger.warn("UserController", "ID inválido en DELETE request: " + path);
            
            // RESPUESTA: JSON estructurado al cliente
            sendJsonResponse(exchange, 400, Map.of(
                "error", "Bad Request",
                "message", "Invalid user ID format",
                "timestamp", LocalDateTime.now()
            ));
            return;
        }

        try {
            // 📝 LOG: Operación iniciada (debugging)
            Logger.debug("UserController", "Procesando DELETE usuario ID: " + userId);
            
            // 🔧 LÓGICA: Ejecutar la operación
            userService.deleteUser(userId);
            
            // 📝 LOG: Éxito (debugging)
            Logger.info("UserController", "Usuario " + userId + " eliminado exitosamente");
            
            // 📤 RESPUESTA: HTTP 204 No Content (estándar REST)
            sendResponse(exchange, 204, "");

        } catch (Exception e) {
            // 📝 LOG: Error detallado para debugging
            Logger.error("UserController", "Error eliminando usuario " + userId, e);
            
            // 📤 RESPUESTA: JSON estructurado vía GlobalExceptionHandler
            GlobalExceptionHandler.handleException(exchange, e);
        }
    }
}
