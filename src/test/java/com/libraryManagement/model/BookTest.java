package com.libraryManagement.model;

import com.libraryManagement.model.enums.CopyStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest extends AbstractPersistenceTest {

    @Test
    public void testBookCreation() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            //Creacion de entidades relacionadas
            log("Creando entidades relacionadas...");

            Genre genre = Genre.builder().name("Ficcion").build();
            Publisher publisher = Publisher.builder().name("Biblioteca").build();
            Author author = Author.builder().name("J.K. Rowling").county("Argentina").build();
            Copy copy = Copy.builder().copyNumber("1").status(CopyStatus.AVAILABLE).build();

            log("Creando entidad Book con metadatos completos...");
            Book book = Book.builder()
                    .title("Harry Potter y la piedra filosofal")
                    .subTitle("Primera entrega de la saga")
                    .publicationDate(LocalDate.of(1999, 5, 1))
                    .language("ES")
                    .isbn("978-9878000001")
                    .edition("1ª edición en español")
                    .genre(genre)
                    .pageCount(256)
                    .seriesName("Harry Potter")
                    .seriesOrder(1)
                    .originalLanguage("EN")
                    .summary("""
                               Harry descubre que es un mago y que está destinado a asistir a Hogwarts, una escuela mágica donde entabla amistad con Ron y Hermione, 
                               y enfrentará los misterios de la piedra filosofal y el regreso del temido Lord Voldemort.
                            """)
                    .tableOfContents("""
                               Capítulo 1: El niño que vivió
                               Capítulo 2: El vidrio que desapareció
                               ...
                               Capítulo final: El hombre con dos caras
                            """)
                    .build();

            log("Estableciendo relaciones bidireccionales...");
            book.addAuthor(author);         // Book ↔ Author
            book.addCopy(copy);            // Book ↔ Copy
            publisher.addPublication(book); // Publisher ↔ Book

            log("Persistiendo entidad Publisher (cascade hacia Book : Genre,Author,Copy  )...");
            session.persist(publisher); // Cascade : -> Book -->(Genre, Author, Copy)

            tx.commit();
            session.clear(); // Limpiar caché para simular acceso desde cero

            log("Recuperando Book desde la base por ISBN...");
            Book persistedBook = session.createQuery("""
                SELECT DISTINCT b FROM Book b
                JOIN FETCH b.authors a
                JOIN FETCH b.publisher p
                JOIN FETCH b.copies c
                WHERE b.isbn = :isbn
            """, Book.class)
                    .setParameter("isbn", "978-9878000001")
                    .getSingleResult();

            log("Validando datos básicos del Book...");
            assertNotNull(persistedBook.getId());
            assertEquals("Harry Potter y la piedra filosofal", persistedBook.getTitle());
            assertEquals("978-9878000001", persistedBook.getIsbn());
            assertEquals("J.K. Rowling", persistedBook.getAuthors().iterator().next().getName());
            assertEquals("Biblioteca", persistedBook.getPublisher().getName());
            assertEquals(1, persistedBook.getCopies().size());
            assertEquals("Ficcion", persistedBook.getGenre().getName());

            log("Validando relaciones inversas...");
            assertTrue(persistedBook.getPublisher().getPublications().contains(persistedBook));

            Author persistedAuthor = persistedBook.getAuthors().iterator().next();
            assertTrue(persistedAuthor.getPublications().contains(persistedBook));

            Copy persistedCopy = persistedBook.getCopies().iterator().next();
            assertEquals(persistedBook, persistedCopy.getPublication());

            log("Validando que no haya duplicados en relaciones...");
            assertEquals(1, new HashSet<>(persistedBook.getAuthors()).size());
            assertEquals(1, new HashSet<>(persistedBook.getCopies()).size());

            log("✅ Test finalizado correctamente.");

        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                tx.rollback();
            }
            System.err.println("❌ Error en la prueba: " + e.getMessage());
            fail("Error en la prueba: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    // Función auxiliar para imprimir mensajes en consola
    private void log(String message) {
        System.out.println("🔹 " + message);
    }
}