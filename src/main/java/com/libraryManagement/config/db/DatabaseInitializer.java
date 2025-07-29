package com.libraryManagement.config.db;

import com.libraryManagement.util.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * INICIALIZADOR DE BASE DE DATOS
 *
 * Maneja la creación automática de la base de datos y configuraciones iniciales
 * de forma robusta y profesional.
 */
public class DatabaseInitializer {

    private static final String DB_NAME = "librarydb";
    private static final String MYSQL_URL_WITHOUT_DB = "jdbc:mysql://localhost:3306/";
    private static final String MYSQL_URL_WITH_DB = MYSQL_URL_WITHOUT_DB + DB_NAME;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    /**
     * INICIALIZA LA BASE DE DATOS COMPLETA
     *
     * 1. Verifica si MySQL está disponible
     * 2. Crea la base de datos si no existe
     * 3. Configura parámetros iniciales
     */
    public static void initializeDatabase() {
        try {
            Logger.info("DatabaseInitializer", "🔍 Verificando conexión a MySQL...");

            // PASO 1: Verificar conexión a MySQL
            if (!isMySQLAvailable()) {
                throw new RuntimeException(" MySQL no está disponible en localhost:3306");
            }

            // PASO 2: Crear base de datos si no existe
            createDatabaseIfNotExists();

            // PASO 3: Verificar conexión a la BD específica
            verifyDatabaseConnection();

            Logger.info("DatabaseInitializer", "Base de datos inicializada correctamente");

        } catch (Exception e) {
            Logger.error("DatabaseInitializer", "Error al inicializar base de datos", e);
            throw new RuntimeException("Error crítico en inicialización de BD", e);
        }
    }

    /**
     * VERIFICA SI MYSQL ESTÁ DISPONIBLE
     */
    private static boolean isMySQLAvailable() {
        try (Connection connection = DriverManager.getConnection(
                MYSQL_URL_WITHOUT_DB + "?allowPublicKeyRetrieval=true&useSSL=false",
                USERNAME, PASSWORD)) {
            return connection != null;
        } catch (SQLException e) {
            Logger.error("DatabaseInitializer", "MySQL no disponible", e);
            return false;
        }
    }

    /**
     * 🏗️ CREA LA BASE DE DATOS SI NO EXISTE
     */
    private static void createDatabaseIfNotExists() throws SQLException {
        String createDatabaseSQL = String.format(
            "CREATE DATABASE IF NOT EXISTS %s CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci",
            DB_NAME
        );

        try (Connection connection = DriverManager.getConnection(
                MYSQL_URL_WITHOUT_DB + "?allowPublicKeyRetrieval=true&useSSL=false",
                USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            Logger.info("DatabaseInitializer", "🏗Creando base de datos si no existe...");
            statement.executeUpdate(createDatabaseSQL);
            Logger.info("DatabaseInitializer", "✅ Base de datos '" + DB_NAME + "' disponible");
        }
    }

    /**
     * ✅ VERIFICA CONEXIÓN A LA BASE DE DATOS ESPECÍFICA
     */
    private static void verifyDatabaseConnection() throws SQLException {
        try (Connection connection = DriverManager.getConnection(
                MYSQL_URL_WITH_DB + "?allowPublicKeyRetrieval=true&useSSL=false",
                USERNAME, PASSWORD)) {

            if (connection != null && !connection.isClosed()) {
                Logger.info("DatabaseInitializer", "✅ Conexión a '" + DB_NAME + "' verificada");
            }
        }
    }

    /**
     * 📊 OBTIENE INFORMACIÓN DE LA BASE DE DATOS
     */
    public static void printDatabaseInfo() {
        try (Connection connection = DriverManager.getConnection(
                MYSQL_URL_WITH_DB + "?allowPublicKeyRetrieval=true&useSSL=false",
                USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            // Información básica
            var metaData = connection.getMetaData();
            Logger.info("DatabaseInitializer", "Base de datos: " + metaData.getDatabaseProductName());
            Logger.info("DatabaseInitializer", "Versión: " + metaData.getDatabaseProductVersion());
            Logger.info("DatabaseInitializer", "Esquema: " + DB_NAME);

        } catch (SQLException e) {
            Logger.error("DatabaseInitializer", "Error obteniendo info de BD", e);
        }
    }
}
