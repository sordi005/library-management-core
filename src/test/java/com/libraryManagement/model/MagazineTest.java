package com.libraryManagement.model;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MagazineTest extends AbstractPersistenceTest {

    @Test
    public void testMagazineCreation() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            log("Creando entidades relacionadas...");

            // Entidad relacionada con atributos mínimos
            Author author = Author.builder()
                    .name("Carlos Ruiz Zafón")
                    .county("España")
                    .build();

            log("Creando entidad Magazine con metadatos completos...");
            Magazine magazine = Magazine.builder()
                    .title("National Geographic")
                    .subTitle("Edición Especial")
                    .publicationDate(LocalDate.of(2025, 1, 1))
                    .language("ES")
                    .issn("0027-9358")
                    .volume("Vol. 47")
                    .edition("Enero 2025")
                    .pageCount(120)
                    .summary("Revista dedicada a la exploración geográfica, historia natural y ciencia.")
                    .build();

            log("Estableciendo relaciones bidireccionales...");
            magazine.addAuthor(author); // Magazine ↔ Author

            log("Persistiendo entidad Magazine (cascade hacia Author)...");
            session.persist(magazine);

            tx.commit();
            session.clear();

            log("Recuperando Magazine desde la base por ISSN...");
            Magazine persistedMagazine = session.createQuery("""
                SELECT DISTINCT m FROM Magazine m
                JOIN FETCH m.authors a
                WHERE m.issn = :issn
                """, Magazine.class)
                    .setParameter("issn", "0027-9358")
                    .getSingleResult();

            log("Validando datos básicos del Magazine...");
            assertNotNull(persistedMagazine.getId());
            assertEquals("National Geographic", persistedMagazine.getTitle());
            assertEquals("Edición Especial", persistedMagazine.getSubTitle());
            assertEquals("0027-9358", persistedMagazine.getIssn());
            assertEquals("Vol. 47", persistedMagazine.getVolume());
            assertEquals(LocalDate.of(2025, 1, 1), persistedMagazine.getPublicationDate());
            assertEquals("ES", persistedMagazine.getLanguage());
            assertEquals("Enero 2025", persistedMagazine.getEdition());
            assertEquals(120, persistedMagazine.getPageCount());
            assertEquals("Revista dedicada a la exploración geográfica, historia natural y ciencia.",
                        persistedMagazine.getSummary());

            log("Validando relaciones Magazine ↔ Author...");
            assertEquals(1, persistedMagazine.getAuthors().size());
            Author persistedAuthor = persistedMagazine.getAuthors().iterator().next();
            assertNotNull(persistedAuthor.getId());
            assertEquals("Carlos Ruiz Zafón", persistedAuthor.getName());
            assertEquals("España", persistedAuthor.getCounty());

            log("Validando relaciones bidireccionales...");
            assertTrue(persistedAuthor.getPublications().contains(persistedMagazine));
            assertTrue(persistedMagazine.getAuthors().contains(persistedAuthor));

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