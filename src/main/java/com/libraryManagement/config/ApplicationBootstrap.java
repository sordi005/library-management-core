package com.libraryManagement.config;

import com.libraryManagement.config.data.SampleDataLoader;
import com.libraryManagement.config.db.JpaConfig;
import com.libraryManagement.config.dependency.DependencyContainer;
import com.libraryManagement.config.server.RouteRegistry;
import com.libraryManagement.config.server.ServerConfiguration;
import com.libraryManagement.controller.UserController;
import com.libraryManagement.util.Logger; // ← AGREGADO: Logger profesional
import com.sun.net.httpserver.HttpServer;

/**
 * Bootstrap principal de la aplicación
 * Responsabilidad: Orquestar la inicialización completa del sistema
 */
public class ApplicationBootstrap {

    private HttpServer server;
    private DependencyContainer container;
    private ServerConfiguration serverConfig;
    private RouteRegistry routeRegistry;

    /**
     * Método principal de arranque
     */
    public void start() {
        try {
            Logger.info("ApplicationBootstrap", "Iniciando Library Management System...");

            // 1. Verificar conexión a base de datos
            initializeDatabase();

            // 2. Cargar datos de muestra si es necesario - Desarrollo
            loadSampleData();

            // 3. Inicializar contenedor de dependencias
            initializeDependencies();

            // 4. Configurar servidor HTTP
            configureServer();

            // 5. Iniciar servidor
            startServer();

            Logger.success("ApplicationBootstrap", "Sistema iniciado correctamente!");
            Logger.info("ApplicationBootstrap", "Servidor ejecutándose en: http://localhost:8080");

        } catch (Exception e) {
            Logger.error("ApplicationBootstrap", "Error fatal durante el arranque", e);
            shutdown();
            throw new RuntimeException(" Error fatal durante el arranque", e);
        }
    }

    /**
     * Inicializa la base de datos
     */
    private void initializeDatabase() {
        try {
            Logger.info("ApplicationBootstrap", "Inicializando conexión a base de datos...");

            // Verificar que JPA se inicialice correctamente
            JpaConfig.getEntityManagerFactory();

            Logger.success("ApplicationBootstrap", "Base de datos conectada");

        } catch (Exception e) {
            throw new RuntimeException(" Error al conectar con la base de datos", e);
        }
    }

    /**
     * Carga datos de muestra si es necesario
     */
    private void loadSampleData() {
        try {
            SampleDataLoader.loadSampleData();
        } catch (Exception e) {
            Logger.error("ApplicationBootstrap", "Error cargando datos de prueba", e);
        }
    }

    /**
     * Inicializa el contenedor de dependencias
     */
    private void initializeDependencies() {
        Logger.info("ApplicationBootstrap", "Inicializando dependencias...");
        container = new DependencyContainer();
        container.initializeDependencies();
    }

    /**
     * Configura el servidor HTTP
     */
    private void configureServer() {
        try {
            Logger.info("ApplicationBootstrap", "Configurando servidor HTTP...");

            serverConfig = new ServerConfiguration();
            server = serverConfig.createServer();

            routeRegistry = new RouteRegistry(server);
            registerRoutes();

            Logger.success("ApplicationBootstrap", "Servidor HTTP configurado");

        } catch (Exception e) {
            throw new RuntimeException(" Error configurando servidor HTTP", e);
        }
    }

    /**
     * Registra todas las rutas
     */
    private void registerRoutes() {
        Logger.debug("ApplicationBootstrap", "Registrando rutas...");

        // Obtener controllers del contenedor
        UserController userController = container.getController(UserController.class);

        // Registrar rutas
        routeRegistry.registerUserRoutes(userController);
        routeRegistry.registerHealthCheck();

        Logger.info("ApplicationBootstrap", "Rutas registradas:");
        Logger.info("ApplicationBootstrap", "   GET/POST/PUT/DELETE /api/users - Gestión de usuarios");
        Logger.info("ApplicationBootstrap", "   GET /health - Health check");
    }

    /**
     * Inicia el servidor
     */
    private void startServer() {
        Logger.info("ApplicationBootstrap", "Iniciando servidor...");
        server.start();
    }

    /**
     * Detiene el sistema
     */
    public void shutdown() {
        Logger.info("ApplicationBootstrap", "Deteniendo sistema...");

        if (server != null) {
            server.stop(0);
        }

        if (container != null) {
            container.shutdown();
        }

        Logger.info("ApplicationBootstrap", "Sistema detenido");
    }
}
