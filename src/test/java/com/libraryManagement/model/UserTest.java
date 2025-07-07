package com.libraryManagement.model;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test; // ✅ JUnit 5import
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class UserTest extends AbstractPersistenceTest{
    private Session session = sessionFactory.openSession();

    @Test
    public void testBookCreation() {
        Transaction tx = session.beginTransaction();

        try {
            // Crear entidades necesarias
            Loan loan = Loan.builder()
                    .startDate(LocalDate.of(2020, 1, 1))
                    .dueDate(LocalDate.of(2020, 1, 14))
                    .build();

            // Crear una entidad User
            User user = User.builder()
                    .firstName("Juan")
                    .lastName("Pérez")
                    .dateOfBirth(LocalDate.of(1990, 1, 1))
                    .dni("12345678A")  // Agregar el DNI
                    .email("JuanPerez@gamil.com")
                    .phone("+5491123456789") // Número de teléfono con código de país
                    .build();

            // Establecer la relación entre User y Loan
            user.addLoan(loan);

            // Persistir la entidad User
            session.persist(user);
            tx.commit();
            session.clear(); // Limpiar caché

            // Verificar que el usuario se haya persistido correctamente
            User persistedUser = session.createQuery("""
            SELECT DISTINCT u FROM User u 
            JOIN FETCH u.loans WHERE u.id = :id
            """, User.class)
                    .setParameter("id", user.getId())
                    .getSingleResult();

            // Afirmar que el usuario persistido no es nulo
            assertNotNull(persistedUser.getId());
            assertNotNull(persistedUser.getDni());

            // Validaciones básicas
            assertEquals("Juan", persistedUser.getFirstName());
            assertEquals("Pérez", persistedUser.getLastName());
            assertEquals(LocalDate.of(1990, 1, 1), persistedUser.getDateOfBirth());
            assertEquals("12345678A", persistedUser.getDni());
            assertEquals("JuanPerez@gamil.com", persistedUser.getEmail());
            assertEquals("+5491123456789", persistedUser.getPhone());

            //Validacion relacional
            assertEquals(1, persistedUser.getLoans().size());
            Loan persistedLoan = persistedUser.getLoans().get(0);
            assertEquals(LocalDate.of(2020, 1, 1),persistedLoan.getStartDate());
            assertEquals(LocalDate.of(2020, 1, 14), persistedLoan.getDueDate());

            assertEquals(persistedUser, persistedLoan.getUser());

    }catch (Exception e){
        if (tx != null) {
            tx.rollback();
        }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
