package com.libraryManagement.domain.user;

import com.libraryManagement.model.User;
import com.libraryManagement.model.Address;
import java.time.LocalDate;
import java.time.Period;

/**
 * UTILIDADES ESPECÍFICAS DEL DOMINIO USER
 *
 * Solo contiene cálculos y transformaciones específicas
 * de la entidad User que NO son validaciones de negocio
 */
public class UserUtils {

    /**
     * Calcula la edad basada en fecha de nacimiento
     */
    public static Integer calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return null;
        }

        LocalDate now = LocalDate.now();
        if (dateOfBirth.isAfter(now)) {
            return 0; // Fecha futura = 0 años
        }

        return Period.between(dateOfBirth, now).getYears();
    }

    /**
     * Extrae el nombre de ciudad de una dirección
     */
    public static String extractCityName(Address address) {
        if (address == null) {
            return null;
        }
        return address.getCity();
    }

    /**
     * Formatea nombre completo del usuario
     */
    public static String formatFullName(String firstName, String lastName) {
        if (firstName == null && lastName == null) {
            return "Nombre no disponible";
        }

        String first = firstName != null ? firstName.trim() : "";
        String last = lastName != null ? lastName.trim() : "";

        if (first.isEmpty() && last.isEmpty()) {
            return "Nombre no disponible";
        }

        return (first + " " + last).trim();
    }

    /**
     * Calcula estado de membresía basado en el estado del usuario
     *
     * NOTA: Este es un cálculo, no una validación de negocio
     */
    public static String calculateMembershipStatus(User user) {
        if (user == null || user.getLoans() == null) {
            return "ACTIVE";
        }

        // Contar préstamos activos
        long activeLoans = user.getLoans().stream()
            .filter(loan -> loan.getReturnedAt() == null)
            .count();

        // Verificar préstamos vencidos
        boolean hasOverdueLoans = user.getLoans().stream()
            .filter(loan -> loan.getReturnedAt() == null)
            .anyMatch(loan -> loan.getDueDate().isBefore(LocalDate.now()));

        if (hasOverdueLoans) {
            return "SUSPENDED";
        } else if (activeLoans >= 3) {
            return "LIMITED";
        } else {
            return "ACTIVE";
        }
    }
}
