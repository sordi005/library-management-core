package com.libraryManagement.config;

import jakarta.persistence.EntityManager;
/**  Clase auxiliar que expone un método para obtener un EntityManager
 * desde el EntityManagerFactory centralizado (configurado en JpaConfig).
 */
public class PersistenceManager {
    /**
     * Crea y retorna un EntityManager nuevo.
     */
    public static EntityManager getEntityManager(){
        try {
            return JpaConfig.getEntityManagerFactory().createEntityManager();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener EntityManager", e);
        }
    }
}
