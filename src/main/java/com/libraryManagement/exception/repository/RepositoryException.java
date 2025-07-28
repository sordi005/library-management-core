package com.libraryManagement.exception.repository;

/**
 * EXCEPCIÓN BASE PARA ERRORES DE REPOSITORY
 *
 * ¿CUÁNDO SE LANZA?
 * - Errores de base de datos (conexión, constraint, timeout)
 * - Problemas técnicos en operaciones CRUD
 *

 */
public class RepositoryException extends RuntimeException {

    private final String operation;  // "SAVE", "FIND", "DELETE"
    private final String entity;     // "User", "Loan", etc.

    public RepositoryException(String message, String operation, String entity) {
        super(String.format("[%s-%s] %s", entity, operation, message));
        this.operation = operation;
        this.entity = entity;
    }

    public RepositoryException(String message, String operation, String entity, Throwable cause) {
        super(String.format("[%s-%s] %s", entity, operation, message), cause);
        this.operation = operation;
        this.entity = entity;
    }

    public String getOperation() {
        return operation;
    }

    public String getEntity() {
        return entity;
    }
}
