package com.libraryManagement.config.db;

import com.libraryManagement.util.Logger; // ← AGREGADO: Logger profesional
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.function.Consumer;
import java.util.function.Function;

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
            Logger.debug("TransactionUtil", "Transacción completada exitosamente");
        }catch (Exception e){
            if(tx.isActive()) {
                tx.rollback();// Revierte en caso de error
                Logger.warn("TransactionUtil", "Transacción revertida debido a error: " + e.getMessage());
            }
            Logger.error("TransactionUtil", "Transacción fallida", e);
            throw new RuntimeException("Transacción fallida", e);
        }finally {
            if(em.isOpen()) em.close();
        }
    }

    /**
     * Ejecuta una función con EntityManager y retorna un resultado dentro de una transacción.
     * Se recomienda para consultas complejas que necesitan transacción.
     */
    public static <T> T executeInTransactionWithResult(Function<EntityManager, T> function){
        EntityManager em = PersistenceManager.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            T result = function.apply(em);
            tx.commit();
            Logger.debug("TransactionUtil", "Transacción con resultado completada exitosamente");
            return result;
        }catch (Exception e){
            if(tx.isActive()) {
                tx.rollback();
                Logger.warn("TransactionUtil", "Transacción con resultado revertida: " + e.getMessage());
            }
            Logger.error("TransactionUtil", "Transacción con resultado fallida", e);
            throw new RuntimeException("Transacción fallida", e);
        }finally {
            if(em.isOpen()) em.close();
        }
    }
}
