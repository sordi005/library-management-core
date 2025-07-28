package com.libraryManagement.exception.service;

/**
 * Excepción base para errores de lógica de negocio en la capa de servicios.
 * Permite distinguir claramente los errores de negocio de los errores técnicos.
 */
public class ServiceException extends RuntimeException {
    private final String entity;
    private final String operation;

    public ServiceException(String entity, String operation, String message) {
        super(message);
        this.entity = entity;
        this.operation = operation;
    }

    public ServiceException(String entity, String operation, String message, Throwable cause) {
        super(message, cause);
        this.entity = entity;
        this.operation = operation;
    }

    public String getEntity() {
        return entity;
    }

    public String getOperation() {
        return operation;
    }
}

