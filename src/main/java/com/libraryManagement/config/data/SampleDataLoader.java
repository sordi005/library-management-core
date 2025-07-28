package com.libraryManagement.config.data;

import com.libraryManagement.config.db.JpaConfig;
import com.libraryManagement.model.Address;
import com.libraryManagement.model.User;
import com.libraryManagement.util.Logger; // ← AGREGADO: Logger profesional
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

/**
 * CARGADOR DE DATOS DE PRUEBA
 *
 * Responsabilidad: Insertar datos de ejemplo en la base de datos
 * para facilitar las pruebas en Postman
 */
public class SampleDataLoader {

    /**
     * Carga datos de ejemplo si la base de datos está vacía
     */
    public static void loadSampleData() {
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            // Verificar si ya hay datos
            Long userCount = em.createQuery("SELECT COUNT(u) FROM User u", Long.class)
                              .getSingleResult();

            if (userCount > 0) {
                Logger.info("SampleDataLoader", "Datos de prueba ya existen, omitiendo carga...");
                em.getTransaction().rollback();
                return;
            }

            Logger.info("SampleDataLoader", "Cargando datos de prueba...");

            // Crear direcciones
            Address[] addresses = createSampleAddresses();
            for (Address address : addresses) {
                em.persist(address);
            }

            // Crear usuarios
            User[] users = createSampleUsers(addresses);
            for (User user : users) {
                em.persist(user);
            }

            em.getTransaction().commit();
            Logger.success("SampleDataLoader", "Datos de prueba cargados exitosamente - " + users.length + " usuarios creados");

        } catch (Exception e) {
            Logger.error("SampleDataLoader", "Error cargando datos de prueba", e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    /**
     * Crea direcciones de ejemplo
     */
    private static Address[] createSampleAddresses() {
        return new Address[]{
            Address.builder()
                .street("Av. Corrientes")
                .number("1234")
                .city("Buenos Aires")
                .province("CABA")
                .country("Argentina")
                .postalCode("C1043")
                .build(),

            Address.builder()
                .street("Calle San Martín")
                .number("567")
                .city("Rosario")
                .province("Santa Fe")
                .country("Argentina")
                .postalCode("S2000")
                .build(),

            Address.builder()
                .street("Av. Libertador")
                .number("890")
                .city("Córdoba")
                .province("Córdoba")
                .country("Argentina")
                .postalCode("X5000")
                .build(),

            Address.builder()
                .street("Calle Belgrano")
                .number("321")
                .city("Mendoza")
                .province("Mendoza")
                .country("Argentina")
                .postalCode("M5500")
                .build(),

            Address.builder()
                .street("Av. 9 de Julio")
                .number("654")
                .city("La Plata")
                .province("Buenos Aires")
                .country("Argentina")
                .postalCode("B1900")
                .build()
        };
    }

    /**
     * Crea usuarios de ejemplo
     */
    private static User[] createSampleUsers(Address[] addresses) {
        return new User[]{
            User.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan.perez@email.com")
                .phoneNumber("11-1234-5678")
                .dni("12345678")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .address(addresses[0])
                .build(),

            User.builder()
                .firstName("María")
                .lastName("González")
                .email("maria.gonzalez@email.com")
                .phoneNumber("11-8765-4321")
                .dni("87654321")
                .dateOfBirth(LocalDate.of(1985, 8, 22))
                .address(addresses[1])
                .build(),

            User.builder()
                .firstName("Carlos")
                .lastName("López")
                .email("carlos.lopez@email.com")
                .phoneNumber("11-5555-1111")
                .dni("11111111")
                .dateOfBirth(LocalDate.of(1992, 12, 3))
                .address(addresses[2])
                .build(),

            User.builder()
                .firstName("Ana")
                .lastName("Martínez")
                .email("ana.martinez@email.com")
                .phoneNumber("11-9999-2222")
                .dni("22222222")
                .dateOfBirth(LocalDate.of(1988, 3, 18))
                .address(addresses[3])
                .build(),

            User.builder()
                .firstName("Pedro")
                .lastName("Rodriguez")
                .email("pedro.rodriguez@email.com")
                .phoneNumber("11-7777-3333")
                .dni("33333333")
                .dateOfBirth(LocalDate.of(1995, 7, 10))
                .address(addresses[4])
                .build()
        };
    }
}
