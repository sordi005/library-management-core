package com.libraryManagement.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * VALIDADOR GENÉRICO PARA DTOs
 *
 * Contiene validaciones TRANSVERSALES que se usan en toda la aplicación
 * NO contiene lógica de negocio específica de entidades
 */
public class DTOValidator {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    /**
     * Valida un DTO usando anotaciones Bean Validation
     */
    public static <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));

            throw new IllegalArgumentException("Datos inválidos: " + errorMessage);
        }
    }

    /**
     * Valida un DTO y retorna lista de errores sin lanzar excepción
     */
    public static <T> Set<String> getValidationErrors(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }

    /**
     * Verifica si un DTO es válido
     */
    public static <T> boolean isValid(T dto) {
        return validator.validate(dto).isEmpty();
    }

    // =====================================================================
    // VALIDACIONES TRANSVERSALES - Usadas en toda la aplicación
    // =====================================================================

    /**
     * Valida formato de DNI argentino (TRANSVERSAL)
     */
    public static boolean isValidArgentinianDNI(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            return false;
        }

        String cleanDNI = dni.replaceAll("[\\s.]", "");
        return cleanDNI.matches("\\d{7,8}");
    }

    /**
     * Valida formato de email (TRANSVERSAL)
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Valida que un string no esté vacío (TRANSVERSAL)
     */
    public static boolean isNotBlank(String text) {
        return text != null && !text.trim().isEmpty();
    }

    /**
     * Valida rango numérico (TRANSVERSAL)
     */
    public static boolean isInRange(Number value, Number min, Number max) {
        if (value == null || min == null || max == null) {
            return false;
        }

        double val = value.doubleValue();
        double minVal = min.doubleValue();
        double maxVal = max.doubleValue();

        return val >= minVal && val <= maxVal;
    }
}
