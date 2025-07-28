package com.libraryManagement.config.server;

import com.libraryManagement.util.Logger; // ← AGREGADO: Logger profesional
import com.sun.net.httpserver.HttpServer;
import com.libraryManagement.controller.UserController;

/**
 * Registro centralizado de rutas y endpoints
 * Responsabilidad: Mapear URLs a controladores específicos
 */
public class RouteRegistry {

    private final HttpServer server;

    public RouteRegistry(HttpServer server) {
        this.server = server;
    }

    /**
     * Registra todas las rutas de usuarios
     */
    public void registerUserRoutes(UserController userController) {
        Logger.debug("RouteRegistry", "Registrando rutas de usuarios...");
        server.createContext("/api/users", userController);
        Logger.info("RouteRegistry", "Rutas de usuarios registradas en /api/users");
    }

    /**
     * Registra endpoint de health check para monitoreo
     */
    public void registerHealthCheck() {
        Logger.debug("RouteRegistry", "Registrando health check...");
        server.createContext("/health", exchange -> {
            String response = "{\"status\":\"UP\",\"service\":\"Library Management API\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        });
        Logger.info("RouteRegistry", "Health check registrado en /health");
    }
}
