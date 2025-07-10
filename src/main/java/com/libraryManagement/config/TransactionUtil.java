package com.libraryManagement.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.function.Consumer;

/**
 * Utilidad para manejar transacciones de forma sencilla.
 * Permite ejecutar acciones dentro de una transacción
 * y manejar errores de forma centralizada.
 */
public class TransactionUtil {
    /**
     * Ejecuta una acción con EntityManager dentro de una transacción.
     * Se recomienda para operaciones como persistencia, actualizaciones o eliminación.
     */
    public static void executeInTransaction(Consumer<EntityManager> action){
        EntityManager em = PersistenceManager.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();// Inicia la transacción
            action.accept(em);// Ejecuta la acción principal
            tx.commit(); // Confirma los cambios
        }catch (Exception e){
            if(tx.isActive()) tx.rollback();// Revierte en caso de error
            throw  new RuntimeException(" -- Transacción fallida", e);
        }finally {
            if(em.isOpen()) em.close();
        }
    }

}
