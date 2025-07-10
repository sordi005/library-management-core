package com.libraryManagement.model;

import com.libraryManagement.model.enums.CopyStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CopyTest extends AbstractPersistenceTest {

    @Test
    public void testCopyCreation() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            log("Creando entidades relacionadas...");

            // Entidades relacionadas con atributos mínimos
            Book book = Book.builder()
                    .title("1984")
                    .isbn("978-9878000004")
                    .build();

            User user = User.builder()
                    .firstName("Ana")
                    .lastName("García")
                    .dateOfBirth(LocalDate.of(1985, 3, 15))
                    .dni("87654321B")
                    .email("ana@email.com")
                    .build();

            Loan loan = Loan.builder()
                    .startDate(LocalDate.of(2025, 1, 10))
                    .dueDate(LocalDate.of(2025, 1, 24))
                    .build();

            LoanLine loanLine = LoanLine.builder()
                    .estimatedReturnDate(LocalDate.of(2025, 1, 24))
                    .build();

            log("Creando entidad Copy con metadatos completos...");
            Copy copy = Copy.builder()
                    .copyNumber("1984-C001")
                    .status(CopyStatus.LOANED)
                    .build();

            book.addCopy(copy);// Book ↔ Copy
            user.addLoan(loan); // User ↔ Loan

            log("Persistiendo entidad Book (cascade hacia Copy y LoanLine)...");
            session.persist(book);
            session.persist(user);

            log("Estableciendo relaciones bidireccionales...");
            copy.addLoanLine(loanLine); // Copy ↔ LoanLine
            loan.addLoanLine(loanLine);

            session.persist(copy);
            

            tx.commit();
            session.clear();

            log("Recuperando Copy desde la base por copyNumber...");
            Copy persistedCopy = session.createQuery("""
                SELECT DISTINCT c FROM Copy c
                JOIN FETCH c.publication p
                JOIN FETCH c.loanLines ll
                JOIN FETCH ll.loan l
                WHERE c.copyNumber = :copyNumber
                """, Copy.class)
                    .setParameter("copyNumber", "1984-C001")
                    .getSingleResult();

            log("Validando datos básicos del Copy...");
            assertNotNull(persistedCopy.getId());
            assertEquals("1984-C001", persistedCopy.getCopyNumber());
            assertEquals(CopyStatus.LOANED, persistedCopy.getStatus());

            log("Validando relación Copy ↔ Publication...");
            assertNotNull(persistedCopy.getPublication().getId());
            assertEquals("1984", persistedCopy.getPublication().getTitle());

            log("Validando relación Copy ↔ LoanLine...");
            assertEquals(1, persistedCopy.getLoanLines().size());
            LoanLine persistedLoanLine = persistedCopy.getLoanLines().get(0);
            assertNotNull(persistedLoanLine.getId());
            assertEquals(LocalDate.of(2025, 1, 24), persistedLoanLine.getEstimatedReturnDate());

            log("Validando relaciones bidireccionales...");
            assertTrue(persistedCopy.getPublication().getCopies().contains(persistedCopy));
            assertEquals(persistedCopy, persistedLoanLine.getCopy());
            assertTrue(persistedCopy.getLoanLines().contains(persistedLoanLine));

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