package com.libraryManagement.model;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenreTest extends AbstractPersistenceTest {

    @Test
    public void testGenreCreation() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            log("Creando entidad Genre con metadatos completos...");
            Genre genre = Genre.builder()
                    .name("Ciencia Ficción")
                    .description("Género literario que se basa en futuros posibles, ciencias y tecnologías avanzadas")
                    .build();

            log("Persistiendo entidad Genre...");
            session.persist(genre);

            tx.commit();
            session.clear();

            log("Recuperando Genre desde la base por ID...");
            Genre persistedGenre = session.createQuery("""
                SELECT g FROM Genre g 
                WHERE g.id = :id
                """, Genre.class)
                    .setParameter("id", genre.getId())
                    .getSingleResult();

            log("Validando datos básicos del Genre...");
            assertNotNull(persistedGenre.getId());
            assertEquals("Ciencia Ficción", persistedGenre.getName());
            assertEquals("Género literario que se basa en futuros posibles, ciencias y tecnologías avanzadas",
                        persistedGenre.getDescription());

            log("✅ Test finalizado correctamente.");

        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                tx.rollback();
            }
            fail("Error en la prueba: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    private void log(String message) {
        System.out.println("🔹 " + message);
    }
}