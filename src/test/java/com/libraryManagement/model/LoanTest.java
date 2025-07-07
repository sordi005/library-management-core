package com.libraryManagement.model;

import com.libraryManagement.enums.CopyStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;

public class LoanTest extends AbstractPersistenceTest {

    @Test
    // Creacion de prestamo.
    public void testLoanCreation() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            //Crear entidades necesarias
            User user = User.builder()
                    .firstName("Juan")
                    .lastName("Pérez")
                    .dateOfBirth(LocalDate.of(1990, 1, 1))
                    .dni("12345678A")  // Agregar el DNI
                    .email("juan@email.com")
                    .build();

            Book book = Book.builder()
                    .title("El Principito")
                    .isbn("978-9878000002")
                    .build();

            Copy copy = Copy.builder()
                    .copyNumber("LP-001")
                    .status(CopyStatus.AVAILABLE)
                    .publication(book)
                    .build();

            //  relacion entre book y copy
            book.addCopy(copy);

            // Crear línea de préstamo
            LoanLine loanLine = LoanLine.builder()
                    .copy(copy)
                    .estimatedReturnDate(LocalDate.now())
                    .build();

            // Crear el préstamo
            Loan loan = Loan.builder()
                    .user(user)
                    .startDate(LocalDate.now())
                    .dueDate(LocalDate.now().plusDays(14))
                    .build();

            // Establecer relaciones bidireccionales
            loan.addLoanLine(loanLine);

            // 4. Persistir entidades
            session.persist(user);
            session.persist(book);
            session.persist(loan);

            tx.commit();
            session.clear();

            // 5. Verificación
            Loan persistedLoan = session.createQuery("""
            
            SELECT DISTINCT l FROM Loan l
            JOIN FETCH l.user u
            JOIN FETCH l.loanLines ll
            JOIN FETCH ll.copy c
            WHERE l.id = :id
            """, Loan.class)
            .setParameter("id", loan.getId())
            .getSingleResult();

            // Validaciones básicas
            assertNotNull(persistedLoan.getId());
            assertEquals("12345678A", persistedLoan.getUser().getDni());
            assertEquals(LocalDate.now(), persistedLoan.getStartDate());
            assertEquals(LocalDate.now().plusDays(14), persistedLoan.getDueDate());


            // Validación de relaciones
            assertEquals(1, persistedLoan.getLoanLines().size());
            LoanLine persistedLine = persistedLoan.getLoanLines().get(0);
            assertEquals("LP-001", persistedLine.getCopy().getCopyNumber());

            // Validación bidireccional
            assertEquals(persistedLoan, persistedLine.getLoan());

        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                tx.rollback();
            }
            fail("Error en la prueba: " + e.getMessage());
        } finally {
            session.close();
        }
    }
    }