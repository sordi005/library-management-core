package com.libraryManagement.model;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class PublisherTest extends AbstractPersistenceTest {

    @Test
    public void testPublisherCreation() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            logStep("Creando entidades relacionadas");

            // Crear Address
            Address address = Address.builder()
                    .street("Av. Corrientes")
                    .number("1234")
                    .city("Buenos Aires")
                    .province("CABA")
                    .country("Argentina")
                    .postalCode("C1043AAZ")
                    .build();

            // Crear Publications
            Book book = Book.builder()
                    .title("Cien años de soledad")
                    .isbn("978-9878000020")
                    .build();

            logStep("Creando Publisher con metadatos completos");

            Publisher publisher = Publisher.builder()
                    .name("Editorial Sudamericana")
                    .webSite("https://www.penguinlibros.com/sudamericana")
                    .email("contacto@sudamericana.com")
                    .phone("+541143267800")
                    .build();

            logStep("Estableciendo relaciones bidireccionales");

            // Relaciones Publisher ↔ Publication
            publisher.addPublication(book);
            // Relaciones Publisher ↔ Address
            publisher.setAddress(address);

            logStep("Persistiendo entidad Publisher");
            session.persist(publisher);

            tx.commit();
            session.clear();

            logStep("Recuperando Publisher desde la base por ID");

            Publisher persistedPublisher = session.createQuery("""
                SELECT DISTINCT p FROM Publisher p
                JOIN FETCH p.publications pub
                JOIN FETCH p.address addr
                WHERE p.id = :id
                """, Publisher.class)
                    .setParameter("id", publisher.getId())
                    .getSingleResult();

            logStep("Validando datos básicos de Publisher");

            // Validaciones básicas
            assertNotNull(persistedPublisher.getId());
            assertEquals("Editorial Sudamericana", persistedPublisher.getName());
            assertEquals("https://www.penguinlibros.com/sudamericana", persistedPublisher.getWebSite());
            assertEquals("contacto@sudamericana.com", persistedPublisher.getEmail());
            assertEquals("+541143267800", persistedPublisher.getPhone());

            logStep("Validando relación Publisher ↔ Address");
            // Validar Address
            assertNotNull(persistedPublisher.getAddress().getId());
            Address persistedAddress = persistedPublisher.getAddress();
            assertEquals("Av. Corrientes", persistedAddress.getStreet());
            assertEquals("1234", persistedAddress.getNumber());
            assertEquals("Buenos Aires", persistedAddress.getCity());
            assertEquals("CABA", persistedAddress.getProvince());
            assertEquals("Argentina", persistedAddress.getCountry());
            assertEquals("C1043AAZ", persistedAddress.getPostalCode());
            //relacion
            assertEquals(persistedPublisher.getAddress().getId(), persistedAddress.getId());

            logStep("Validando relaciones Publisher ↔ Publication");
            // Validar Publications
            assertEquals(1, persistedPublisher.getPublications().size());
            Publication persistedPublication = persistedPublisher.getPublications().iterator().next();
            assertNotNull(persistedPublication.getId());
            assertEquals("Cien años de soledad", persistedPublication.getTitle());
            logStep("Validando relaciones bidireccionales");

            // Validar relaciones inversas Publication → Publisher
            assertEquals(persistedPublisher,persistedPublication.getPublisher());
            assertTrue(persistedPublisher.getPublications().contains(persistedPublication));

            logStep("Validando que no haya duplicados en relaciones");
            // Validar que no haya duplicados en las relaciones
            assertEquals(1, new HashSet<>(persistedPublisher.getPublications()).size());


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

    private void logStep(String message) {
        System.out.println("🔹-------------------- " + message + " --------------------🔹");
    }

    private void log(String message) {
        System.out.println("🔹 " + message);
    }
}