package com.libraryManagement.config.db;

import com.libraryManagement.util.Logger; // ← AGREGADO: Logger profesional
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase de configuración que inicializa y expone el EntityManagerFactory
 * de forma centralizada. Se utiliza para gestionar la conexión JPA con la base de datos.
 */
public class JpaConfig {

    // Instancia única y global de EntityManagerFactory
    private static final EntityManagerFactory emf;

    static {
        try {
            Logger.debug("JpaConfig", "Inicializando EntityManagerFactory...");
            // Crea el EntityManagerFactory a partir del archivo persistence.xml
            // El nombre 'libraryPU' debe coincidir con el persistence-unit definido
            emf = Persistence.createEntityManagerFactory("libraryPU");
            Logger.success("JpaConfig", "EntityManagerFactory inicializado correctamente");
        } catch (Exception e) {
            Logger.error("JpaConfig", "Error al inicializar EntityManagerFactory", e);
            throw new RuntimeException("❌ Error al inicializar EntityManagerFactory", e);
        }
    }

    /**
     * Retorna la instancia de EntityManagerFactory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    /**
     * Cierra el EntityManagerFactory al finalizar la aplicación
     */
    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            Logger.info("JpaConfig", "Cerrando EntityManagerFactory...");
            emf.close();
            Logger.info("JpaConfig", "EntityManagerFactory cerrado");
        }
    }
}
