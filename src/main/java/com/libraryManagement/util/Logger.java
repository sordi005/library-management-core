package com.libraryManagement.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SISTEMA DE LOGGING SIMPLE PERO PROFESIONAL
 *
 * En lugar de usar System.out.println disperso por el código,
 * centralizamos el logging con diferentes niveles.
 */
public class Logger {

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // 🔧 CONFIGURACIÓN: Controla qué logs se muestran
    private static final boolean PRODUCTION_MODE = false; // Cambiar a true para producción
    private static final Level MIN_LEVEL = PRODUCTION_MODE ? Level.WARN : Level.DEBUG;

    public enum Level {
        INFO("📘", "INFO", 1),
        WARN("⚠️", "WARN", 2),
        ERROR("❌", "ERROR", 3),
        DEBUG("🔍", "DEBUG", 0),
        SUCCESS("✅", "SUCCESS", 1);

        private final String emoji;
        private final String text;
        private final int priority;

        Level(String emoji, String text, int priority) {
            this.emoji = emoji;
            this.text = text;
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
    }

    /**
     * Log de información general
     */
    public static void info(String message) {
        log(Level.INFO, message, null);
    }

    public static void info(String operation, String message) {
        log(Level.INFO, operation + ": " + message, null);
    }

    /**
     * Log de advertencias
     */
    public static void warn(String message) {
        log(Level.WARN, message, null);
    }

    public static void warn(String operation, String message) {
        log(Level.WARN, operation + ": " + message, null);
    }

    /**
     * Log de errores
     */
    public static void error(String message) {
        log(Level.ERROR, message, null);
    }

    public static void error(String message, Throwable throwable) {
        log(Level.ERROR, message, throwable);
    }

    public static void error(String operation, String message, Throwable throwable) {
        log(Level.ERROR, operation + ": " + message, throwable);
    }

    /**
     * Log de éxito
     */
    public static void success(String message) {
        log(Level.SUCCESS, message, null);
    }

    public static void success(String operation, String message) {
        log(Level.SUCCESS, operation + ": " + message, null);
    }

    /**
     * Log de debug (solo en desarrollo)
     */
    public static void debug(String message) {
        if (isDebugEnabled()) {
            log(Level.DEBUG, message, null);
        }
    }

    public static void debug(String operation, String message) {
        if (isDebugEnabled()) {
            log(Level.DEBUG, operation + ": " + message, null);
        }
    }

    /**
     * Método principal de logging con filtrado por nivel
     */
    private static void log(Level level, String message, Throwable throwable) {
        // 🔧 FILTRAR: Solo mostrar logs del nivel apropiado
        if (level.getPriority() < MIN_LEVEL.getPriority()) {
            return; // No mostrar logs de nivel bajo en producción
        }

        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String logLine = String.format("%s [%s] %s %s",
            timestamp, level.text, level.emoji, message);

        // Usar System.err para errores, System.out para el resto
        if (level == Level.ERROR) {
            System.err.println(logLine);
            if (throwable != null) {
                throwable.printStackTrace();
            }
        } else {
            System.out.println(logLine);
        }
    }

    /**
     * Verifica si el debug está habilitado
     */
    private static boolean isDebugEnabled() {
        // En un proyecto real, esto vendría de configuración
        return "true".equals(System.getProperty("app.debug", "false"));
    }

    /**
     * Log de inicio de operación
     */
    public static void startOperation(String operation, Object... params) {
        if (params.length > 0) {
            info(operation, "Iniciando con parámetros: " + java.util.Arrays.toString(params));
        } else {
            info(operation, "Iniciando");
        }
    }

    /**
     * Log de fin exitoso de operación
     */
    public static void endOperation(String operation) {
        success(operation, "Completado exitosamente");
    }

    /**
     * Log de fin con error de operación
     */
    public static void failOperation(String operation, String reason) {
        error(operation + ": Falló - " + reason);
    }
}
