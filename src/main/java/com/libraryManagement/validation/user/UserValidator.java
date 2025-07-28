package com.libraryManagement.validation.user;

import com.libraryManagement.model.User;
import com.libraryManagement.validation.DTOValidator;

import java.time.LocalDate;
import java.time.Period;

/**
 * VALIDACIONES ESPECÍFICAS PARA USER
 *
 * Contiene reglas de negocio y validaciones complejas
 * que son específicas del dominio de usuarios
 */
public class UserValidator {

    /**
     * Valida si un usuario puede solicitar préstamos
     *
     * REGLAS DE NEGOCIO:
     * - Debe ser mayor de edad
     * - No debe tener préstamos vencidos
     * - No debe exceder el límite de préstamos activos
     */
    public static boolean canBorrow(User user) {
        if (user == null) {
            return false;
        }

        // Validar mayoría de edad
        if (!isAdult(user.getDateOfBirth())) {
            return false;
        }

        // Validar préstamos vencidos
        if (hasOverdueLoans(user)) {
            return false;
        }

        // Validar límite de préstamos activos (máximo 3)
        if (getActiveLoanCount(user) >= 3) {
            return false;
        }

        return true;
    }

    /**
     * Verifica si el usuario es mayor de edad
     */
    public static boolean isAdult(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }

        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        return age >= 18;
    }

    /**
     * Verifica si el usuario tiene préstamos vencidos
     */
    public static boolean hasOverdueLoans(User user) {
        if (user == null || user.getLoans() == null) {
            return false;
        }

        return user.getLoans().stream()
            .filter(loan -> loan.getReturnedAt() == null) // Solo préstamos activos
            .anyMatch(loan -> loan.getDueDate().isBefore(LocalDate.now()));
    }

    /**
     * Cuenta préstamos activos del usuario
     */
    public static int getActiveLoanCount(User user) {
        if (user == null || user.getLoans() == null) {
            return 0;
        }

        return (int) user.getLoans().stream()
            .filter(loan -> loan.getReturnedAt() == null)
            .count();
    }

    /**
     * Valida el DNI usando DTOValidator genérico
     */
    public static boolean isValidDNI(String dni) {
        return DTOValidator.isValidArgentinianDNI(dni);
    }

    /**
     * Valida el email usando DTOValidator genérico
     */
    public static boolean isValidEmail(String email) {
        return DTOValidator.isValidEmail(email);
    }
}
