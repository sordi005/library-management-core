package com.libraryManagement.config.server;

import com.libraryManagement.util.Logger; // ← AGREGADO: Logger profesional
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Configuración del servidor HTTP
 * Responsabilidad: Crear y configurar el servidor HTTP con sus parámetros
 */
public class ServerConfiguration {

    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_BACKLOG = 0; // Sin límite de conexiones pendientes
    private static final int THREAD_POOL_SIZE = 10;

    private final int port;
    private final int backlog;
    private final int threadPoolSize;

    // Constructor con valores por defecto
    public ServerConfiguration() {
        this(DEFAULT_PORT, DEFAULT_BACKLOG, THREAD_POOL_SIZE);
    }

    // Constructor personalizable
    public ServerConfiguration(int port, int backlog, int threadPoolSize) {
        this.port = port;
        this.backlog = backlog;
        this.threadPoolSize = threadPoolSize;
    }

    /**
     * Crea y configura el servidor HTTP
     */
    public HttpServer createServer() throws IOException {
        try {
            Logger.debug("ServerConfiguration", "Creando servidor en puerto " + port);

            HttpServer server = HttpServer.create(new InetSocketAddress(port), backlog);

            // Configurar pool de threads
            server.setExecutor(Executors.newFixedThreadPool(threadPoolSize));

            Logger.success("ServerConfiguration", "Servidor HTTP creado exitosamente - Puerto: " + port + ", Threads: " + threadPoolSize);
            return server;

        } catch (IOException e) {
            Logger.error("ServerConfiguration", "Error creando servidor HTTP en puerto " + port, e);
            throw e;
        }
    }

    // Getters
    public int getPort() { return port; }
    public int getBacklog() { return backlog; }
    public int getThreadPoolSize() { return threadPoolSize; }
}
