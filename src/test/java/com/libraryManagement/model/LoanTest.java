package com.libraryManagement.model;

import com.libraryManagement.enums.CopyStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import static java.time.LocalDate.*;
import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;

public class LoanTest extends AbstractPersistenceTest {

    private void logStep(String message) {
        System.out.println("🔹-------------------- " + message + " --------------------🔹");
    }

    @Test
    // Creacion de prestamo.
    public void testLoanCreation() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
           //Crear entidades necesarias

            logStep("Creación de Book");
            Book book = Book.builder()
                    .title("El Principito")
                    .isbn("978-9878000002")
                    .build();

            logStep("Creación de Copy");
            Copy copy = Copy.builder()
                    .copyNumber("LP-001")
                    .status(CopyStatus.AVAILABLE)
                    .build();



            logStep("Creación de LoanLine");
            // Crear línea de préstamo
            LoanLine loanLine = LoanLine.builder()
                    .estimatedReturnDate(LocalDate.of(2020, 1, 14))
                    .build();

            logStep("Creación de User");
            User user = User.builder()
                    .firstName("Juan")
                    .lastName("Pérez")
                    .dateOfBirth(of(1990, 1, 1))
                    .dni("12345678A")  // Agregar el DNI
                    .email("juan@email.com")
                    .build();

            logStep("Creaciónd de Loan");
            // Crear el préstamo
            Loan loan = Loan.builder()
                    .startDate(LocalDate.of(2020, 1, 1))
                    .dueDate(LocalDate.of(2020, 1, 14))
                    .build();

            // Establecer relaciones bidireccionales

            logStep("Agregando una copy a Book");
            book.addCopy(copy); //  relacion bidireccional entre book y copy

            logStep("Persistiendo Book");
            session.persist(book); // Persistir Book, lo que cascada a Copy

            logStep("Agregando LoanLine a Copy");
            copy.addLoanLine(loanLine); // Relación bidireccional entre Copy y LoanLine

            logStep("Agregando LoanLine a Loan ");
            loan.addLoanLine(loanLine); // relacion bidireccional con loan persistido

            logStep("Agregando Loan a User");
            user.addLoan(loan); // Relación bidireccional entre User y Loan

            // Persistir entidades
            logStep("Persistiendo User");
            session.persist(user);



            tx.commit();
            session.clear();

            // 5. Verificación
            Loan persistedLoan = session.createQuery("""
            
            SELECT DISTINCT l FROM Loan l
            JOIN FETCH l.user u
            JOIN FETCH l.loanLines ll
            JOIN FETCH ll.copy co
            JOIN FETCH co.publication p
            WHERE l.id = :id      
            """, Loan.class)
            .setParameter("id", loan.getId())
            .getSingleResult();

            // Validaciones básicas
            assertNotNull(persistedLoan.getId());
            assertEquals(LocalDate.of(2020, 1, 1), persistedLoan.getStartDate());
            assertEquals(LocalDate.of(2020, 1, 14), persistedLoan.getDueDate());


            // Validación de relaciones
            //LoanLine
            assertEquals(1, persistedLoan.getLoanLines().size());
            LoanLine persistedLine = persistedLoan.getLoanLines().get(0);
            assertNotNull(persistedLine.getId());
            assertEquals(LocalDate.of(2020, 1, 14),persistedLine.getEstimatedReturnDate());

            //validadaciones bidireccionales
            assertEquals(persistedLoan,persistedLine.getLoan());
            assertEquals(persistedLine.getLoan().getId(), persistedLoan.getId());
            assertTrue(persistedLoan.getLoanLines().contains(loanLine));

            // user
            assertEquals(1, persistedLoan.getUser().getLoans().size());
            User persistedUser = persistedLoan.getUser();
            assertNotNull(persistedUser.getId());
            assertEquals("12345678A",persistedUser.getDni());

            //validadaciones bidireccionales
            assertTrue(persistedUser.getLoans().contains(loan));
            assertEquals(persistedUser.getId(), persistedLoan.getUser().getId());

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