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

            // Crear Genre
            Genre genre = Genre.builder()
                    .name("Literatura")
                    .description("Obras literarias clásicas y contemporáneas")
                    .build();

            // Crear Author
            Author author = Author.builder()
                    .name("Gabriel García Márquez")
                    .county("Colombia")
                    .build();

            // Crear Publications
            Book book1 = Book.builder()
                    .title("Cien años de soledad")
                    .subTitle("Obra maestra del realismo mágico")
                    .publicationDate(LocalDate.of(1967, 5, 30))
                    .language("ES")
                    .isbn("978-9878000020")
                    .edition("1ª edición")
                    .pageCount(417)
                    .seriesName("Obras completas")
                    .seriesOrder(1)
                    .originalLanguage("ES")
                    .genre(genre)
                    .build();

            Book book2 = Book.builder()
                    .title("El amor en los tiempos del cólera")
                    .subTitle("Una historia de amor eterna")
                    .publicationDate(LocalDate.of(1985, 3, 15))
                    .language("ES")
                    .isbn("978-9878000021")
                    .edition("1ª edición")
                    .pageCount(464)
                    .seriesName("Obras completas")
                    .seriesOrder(2)
                    .originalLanguage("ES")
                    .genre(genre)
                    .build();

            logStep("Creando Publisher con metadatos completos");

            Publisher publisher = Publisher.builder()
                    .name("Editorial Sudamericana")
                    .webSite("https://www.penguinlibros.com/sudamericana")
                    .email("contacto@sudamericana.com")
                    .phone("+541143267800")
                    .address(address)
                    .build();

            logStep("Estableciendo relaciones bidireccionales");

            // Relaciones Author ↔ Publication
            book1.addAuthor(author);
            book2.addAuthor(author);

            // Relaciones Publisher ↔ Publication
            publisher.addPublication(book1);
            publisher.addPublication(book2);

            logStep("Persistiendo entidad Publisher");
            session.persist(publisher);

            tx.commit();
            session.clear();

            logStep("Recuperando Publisher desde la base por ID");

            Publisher persistedPublisher = session.createQuery("""
                SELECT DISTINCT p FROM Publisher p
                JOIN FETCH p.publications pub
                JOIN FETCH pub.authors a
                LEFT JOIN FETCH p.address addr
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
            assertNotNull(persistedPublisher.getAddress());
            Address persistedAddress = persistedPublisher.getAddress();
            assertEquals("Av. Corrientes", persistedAddress.getStreet());
            assertEquals("1234", persistedAddress.getNumber());
            assertEquals("Buenos Aires", persistedAddress.getCity());
            assertEquals("CABA", persistedAddress.getProvince());
            assertEquals("Argentina", persistedAddress.getCountry());
            assertEquals("C1043AAZ", persistedAddress.getPostalCode());

            logStep("Validando relaciones Publisher ↔ Publication");

            // Validar Publications
            assertEquals(2, persistedPublisher.getPublications().size());
            assertTrue(persistedPublisher.getPublications().stream()
                    .anyMatch(pub -> "Cien años de soledad".equals(pub.getTitle())));
            assertTrue(persistedPublisher.getPublications().stream()
                    .anyMatch(pub -> "El amor en los tiempos del cólera".equals(pub.getTitle())));

            logStep("Validando relaciones bidireccionales");

            // Validar relaciones inversas Publication → Publisher
            for (Publication publication : persistedPublisher.getPublications()) {
                assertEquals(persistedPublisher, publication.getPublisher());
                assertEquals(persistedPublisher.getId(), publication.getPublisher().getId());
            }

            logStep("Validando datos específicos de cada Publication");

            // Validar detalles específicos de cada libro
            Publication cienAnos = persistedPublisher.getPublications().stream()
                    .filter(pub -> "Cien años de soledad".equals(pub.getTitle()))
                    .findFirst()
                    .orElse(null);

            assertNotNull(cienAnos);
            assertTrue(cienAnos instanceof Book);
            Book cienAnosBook = (Book) cienAnos;
            assertEquals("978-9878000020", cienAnosBook.getIsbn());
            assertEquals(LocalDate.of(1967, 5, 30), cienAnosBook.getPublicationDate());
            assertEquals(417, cienAnosBook.getPageCount());
            assertEquals("Literatura", cienAnosBook.getGenre().getName());

            // Validar autores
            assertEquals(1, cienAnos.getAuthors().size());
            assertTrue(cienAnos.getAuthors().stream()
                    .anyMatch(a -> "Gabriel García Márquez".equals(a.getName())));

            logStep("Validando que no haya duplicados");

            assertEquals(2, new HashSet<>(persistedPublisher.getPublications()).size());

            logStep("Validando relaciones Author ↔ Publication a través de Publisher");

            // Validar que el autor tiene ambas publicaciones
            for (Publication publication : persistedPublisher.getPublications()) {
                for (Author pubAuthor : publication.getAuthors()) {
                    assertTrue(pubAuthor.getPublications().contains(publication));
                }
            }

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