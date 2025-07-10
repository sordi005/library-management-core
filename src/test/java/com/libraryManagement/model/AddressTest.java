package com.libraryManagement.model;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressTest extends AbstractPersistenceTest {

    @Test
    public void testAddressCreation() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            log("Creando entidad Address con metadatos completos...");
            Address address = Address.builder()
                    .street("Av. 9 de Julio")
                    .number("1000")
                    .city("Buenos Aires")
                    .province("CABA")
                    .country("Argentina")
                    .postalCode("C1043AAX")
                    .build();

            log("Persistiendo entidad Address...");
            session.persist(address);

            tx.commit();
            session.clear();

            log("Recuperando Address desde la base por ID...");
            Address persistedAddress = session.createQuery("""
                SELECT a FROM Address a 
                WHERE a.id = :id
                """, Address.class)
                    .setParameter("id", address.getId())
                    .getSingleResult();

            log("Validando datos básicos del Address...");
            assertNotNull(persistedAddress.getId());
            assertEquals("Av. 9 de Julio", persistedAddress.getStreet());
            assertEquals("1000", persistedAddress.getNumber());
            assertEquals("Buenos Aires", persistedAddress.getCity());
            assertEquals("CABA", persistedAddress.getProvince());
            assertEquals("Argentina", persistedAddress.getCountry());
            assertEquals("C1043AAX", persistedAddress.getPostalCode());

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