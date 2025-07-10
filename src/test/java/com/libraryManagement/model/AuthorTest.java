package com.libraryManagement.model;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorTest extends AbstractPersistenceTest {

    @Test
    public void testAuthorCreation() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            log("Creando entidades relacionadas...");

            // Entidad relacionada con atributos mínimos
            Book book = Book.builder()
                    .title("Cien años de soledad")
                    .isbn("978-9878000003")
                    .build();

            log("Creando entidad Author con metadatos completos...");
            Author author = Author.builder()
                    .name("Gabriel García Márquez")
                    .county("Colombia")
                    .build();

            log("Estableciendo relaciones bidireccionales...");
            author.addPublication(book); // Author ↔ Publication

            log("Persistiendo entidad Author (cascade hacia Publication)...");
            session.persist(book);

            tx.commit();
            session.clear();

            log("Recuperando Author desde la base por ID...");
            Author persistedAuthor = session.createQuery("""
                SELECT DISTINCT a FROM Author a
                JOIN FETCH a.publications p
                WHERE a.id = :id
                """, Author.class)
                    .setParameter("id", author.getId())
                    .getSingleResult();

            log("Validando datos básicos del Author...");
            assertNotNull(persistedAuthor.getId());
            assertEquals("Gabriel García Márquez", persistedAuthor.getName());
            assertEquals("Colombia", persistedAuthor.getCounty());

            log("Validando relaciones Author ↔ Publication...");
            assertEquals(1, persistedAuthor.getPublications().size());
            Publication persistedPublication = persistedAuthor.getPublications().get(0);
            assertNotNull(persistedPublication.getId());
            assertEquals("Cien años de soledad", persistedPublication.getTitle());

            log("Validando relaciones bidireccionales...");
            assertTrue(persistedPublication.getAuthors().contains(persistedAuthor));
            assertTrue(persistedAuthor.getPublications().contains(persistedPublication));

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