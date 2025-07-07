package com.libraryManagement.model;

import com.libraryManagement.enums.CopyStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test; // ✅ JUnit 5import
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashSet;

public class BookTest extends AbstractPersistenceTest {
    private Session session = sessionFactory.openSession();
    @Test
    public void testBookCreation() {
        Transaction tx = session.beginTransaction();
        try {

            Genre genre = Genre.builder().name("Ficcion").build();
            Publisher publisher = Publisher.builder().name("Biblioteca").build();
            Author author = Author.builder().name("J.K. Rowling").county("Argentina").build();
            Copy copy = Copy.builder().copyNumber("1").status(CopyStatus.AVAILABLE).build();

            Book book = Book.builder()
                    .title("Harry Potter y la piedra filosofal") // Título completo
                    .subTitle("Primera entrega de la saga") // Opcional, si querés distinguir ediciones
                    .publicationDate(LocalDate.of(1999, 5, 1)) // Fecha de publicación en española
                    .language("ES") // Español
                    .isbn("978-9878000001") // ISBN válido para edición en español
                    .edition("1ª edición en español") // Puede variar según la editorial
                    .genre(genre)
                    .pageCount(256) // Número de páginas típicas
                    .seriesName("Harry Potter") // Nombre de la saga
                    .seriesOrder(1) // Primer libro de la serie
                    .originalLanguage("EN") // Idioma original
                    .summary("""
                               Harry descubre que es un mago y que está destinado a asistir a Hogwarts, una escuela mágica donde entabla amistad con Ron y Hermione, 
                               y enfrentará los misterios de la piedra filosofal y el regreso del temido Lord Voldemort.
                            """) // Sinopsis
                    .tableOfContents("""
                               Capítulo 1: El niño que vivió
                               Capítulo 2: El vidrio que desapareció
                               ...
                               Capítulo final: El hombre con dos caras
                            """) // Índice estimado
                    .build();

            // Establecimiento de relaciones
            book.addAuthor(author);
            book.addCopy(copy);
            publisher.addPublication(book);

            session.persist(book);
            tx.commit();

            session.clear(); // Limpiar caché para simular nuevo acceso

            // 5. Verificación con HQL
            Book persistedBook = session.createQuery("""
                SELECT DISTINCT b FROM Book b
                JOIN FETCH b.authors a
                JOIN FETCH b.publisher p
                JOIN FETCH b.copies c
                WHERE b.isbn = :isbn
            """, Book.class)
            .setParameter("isbn", "978-9878000001")
            .getSingleResult();

            // Validación de ID (persistencia exitosa)
            assertNotNull(persistedBook.getId());

            // Validación de relaciones directas
            assertEquals("Harry Potter y la piedra filosofal", persistedBook.getTitle());
            assertEquals("978-9878000001",persistedBook.getIsbn());
            assertEquals("J.K. Rowling", persistedBook.getAuthors().iterator().next().getName());
            assertEquals("Biblioteca", persistedBook.getPublisher().getName());
            assertEquals(1, persistedBook.getCopies().size());
            assertEquals("Ficcion",persistedBook.getGenre().getName());

            // Validación de relaciones inversas
            //Publisher
            assertTrue(persistedBook.getPublisher().getPublications().contains(persistedBook));
            //Author
            Author persitedAuthor = persistedBook.getAuthors().iterator().next();
            assertTrue(persitedAuthor.getPublications().contains(persistedBook));
            //Copy
            Copy persitedCopy = persistedBook.getCopies().iterator().next();
            assertEquals(persistedBook, persitedCopy.getPublication());


            // Validación de duplicados
            assertEquals(1,new HashSet<>(persistedBook.getAuthors()).size());
            assertEquals(1,new HashSet<>(persistedBook.getCopies()).size());

        }catch (Exception e){
            if (tx != null && tx.getStatus().canRollback()){
                tx.rollback();
            }
            fail("Error en la prueba: " + e.getMessage());
        }finally {
            session.close();
        }
    }
}
