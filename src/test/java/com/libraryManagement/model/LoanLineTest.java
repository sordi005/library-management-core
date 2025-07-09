package com.libraryManagement.model;

import com.libraryManagement.model.enums.CopyStatus;
import java.time.LocalDate;

import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoanLineTest extends AbstractPersistenceTest {

 /**
  *Test básico de creación de LoanLine
  */

    @Test
    public void testLoanLineCreation() {
        Transaction tx = session.beginTransaction();
        try {
            // Crear entidades raíz
            log("Creando entidades principales USER, BOOK, COPY, LOAN y LOANLINE...");

            Book book = Book.builder()
                    .title("El Principito")
                    .isbn("978-9878000002")
                    .build();

            Copy copy = Copy.builder()
                    .copyNumber("LP-001")
                    .status(CopyStatus.AVAILABLE)
                    .build();

            User user = User.builder()
                    .firstName("Juan")
                    .lastName("Pérez")
                    .dateOfBirth(LocalDate.of(1990, 1, 1))
                    .dni("12345678A")
                    .email("juanPerz@gmail.com")
                    .build();

            LoanLine loanLine = LoanLine.builder()
                    .estimatedReturnDate(LocalDate.of(2025, 7, 7))
                    .build();

            Loan loan = Loan.builder()
                    .startDate(LocalDate.of(2025, 7, 7))
                    .dueDate(LocalDate.of(2025, 7, 21))
                    .build();

            log("Estableciendo relaciones...");
            book.addCopy(copy);// sincroniza Copy → Book
            user.addLoan(loan); // sincroniza Loan → User
            loan.addLoanLine(loanLine); // LoanLine → Loan
            copy.addLoanLine(loanLine); // LoanLine → Copy

            log("Persistiendo entidades raíz user | book");
            session.persist(user); // cascade → Loan → LoanLine°|
            session.persist(book); // cascade → Copy

            tx.commit();
            session.clear();

            // Validar que LoanLine fue persistido correctamente
            log("Recuperando LoanLine de la DB");
            LoanLine persistedLoanLine = session.createQuery("""
                SELECT DISTINCT ll FROM LoanLine ll 
                JOIN FETCH ll.loan l
                JOIN FETCH ll.copy c
                WHERE ll.id = :id
                """, LoanLine.class)
                    .setParameter("id", loanLine.getId())
                    .getSingleResult();

            log("Validación de datos persistidos...");

            // ✅ Validar que LoanLine fue persistido correctamente
            assertNotNull(persistedLoanLine.getId()); // Debe tener un ID asignado por la base
            assertEquals(LocalDate.of(2025, 7, 7), persistedLoanLine.getEstimatedReturnDate()); // Fecha estimada de devolución correcta

            // Validar datos del Copy asociado
            Copy persistedCopy = persistedLoanLine.getCopy();
            assertEquals("LP-001", persistedCopy.getCopyNumber()); // Número de copia correcto
            assertEquals(CopyStatus.AVAILABLE, persistedCopy.getStatus()); // Estado correcto
            assertNotNull(persistedCopy.getPublication()); // Debe estar asociado a una publicación (Book)
            assertTrue(persistedCopy.getLoanLines().contains(loanLine)); // La relación bidireccional Copy → LoanLine debe estar sincronizada

            // Validar datos del Loan
            Loan persistedLoan = persistedLoanLine.getLoan();
            assertEquals(LocalDate.of(2025, 7, 7), persistedLoan.getStartDate()); // Fecha de inicio correcta
            assertEquals(LocalDate.of(2025, 7, 21), persistedLoan.getDueDate()); // Fecha de vencimiento correcta
            assertTrue(persistedLoan.getLoanLines().contains(loanLine)); // La relación bidireccional Loan → LoanLine debe estar sincronizada

            // Validar datos del User asociado al Loan
            assertEquals("Juan", persistedLoan.getUser().getFirstName()); // Nombre del usuario correcto

            // Validar datos del Book asociado al Copy
            assertEquals("El Principito", persistedCopy.getPublication().getTitle()); // Título del libro correcto

            log("✅ Test finalizado correctamente.");

        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                tx.rollback();
            }
            System.err.println("❌ Error en la prueba: " + e.getMessage());
            fail("Error en la prueba: " + e.getMessage());
        } finally {
            closeSession();
        }
    }

    // Función simple para imprimir mensajes en consola
    private void log(String message) {
        System.out.println("🔹 " + message);
    }
}
