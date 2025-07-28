package com.libraryManagement.domain.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para estadísticas de préstamos de un usuario
 *
 * Contiene métricas calculadas sobre los préstamos de un usuario:
 * - Totales y activos
 * - Vencidos y completados
 * - Promedios y tendencias
 *
 * Se usa en contextos donde necesitas información agregada
 * sin cargar todas las entidades completas
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanUtils {

    // =====================================================================
    // ESTADÍSTICAS BÁSICAS
    // =====================================================================

    /** Número total de préstamos históricos */
    private Integer totalLoans;

    /** Número de préstamos actualmente activos */
    private Integer activeLoans;

    /** Número de préstamos vencidos (sin devolver) */
    private Integer overdueLoans;

    /** Número de préstamos completados (devueltos) */
    private Integer completedLoans;

    // =====================================================================
    // ESTADÍSTICAS CALCULADAS
    // =====================================================================

    /** Duración promedio de préstamos en días */
    private Double averageLoanDurationDays;

    /** Fecha del último préstamo realizado */
    private LocalDate lastLoanDate;

    /** Fecha de devolución más próxima */
    private LocalDate nextDueDate;

    /** Número total de libros diferentes prestados */
    private Integer uniqueBooksLoaned;

    // =====================================================================
    // INDICADORES DE ESTADO
    // =====================================================================

    /** Si el usuario tiene préstamos vencidos */
    private Boolean hasOverdueLoans;

    /** Si el usuario puede pedir más préstamos */
    private Boolean canBorrowMore;

    /** Estado del usuario: ACTIVE, LIMITED, SUSPENDED */
    private String membershipStatus;

    /** Límite de préstamos simultaneos para este usuario */
    private Integer maxSimultaneousLoans;

    // =====================================================================
    // MÉTODOS DE UTILIDAD
    // =====================================================================

    /**
     * Verifica si el usuario está en buen estado
     * @return true si puede pedir préstamos sin restricciones
     */
    public boolean isInGoodStanding() {
        return !hasOverdueLoans && canBorrowMore && "ACTIVE".equals(membershipStatus);
    }

    /**
     * Calcula el porcentaje de préstamos completados a tiempo
     * @return Porcentaje de 0.0 a 100.0
     */
    public Double getCompletionRate() {
        if (totalLoans == null || totalLoans == 0) return 100.0;
        if (completedLoans == null) return 0.0;
        return (completedLoans.doubleValue() / totalLoans.doubleValue()) * 100.0;
    }

    /**
     * Calcula cuántos préstamos más puede hacer el usuario
     * @return Número de préstamos disponibles
     */
    public Integer getAvailableLoansCount() {
        if (!canBorrowMore || maxSimultaneousLoans == null || activeLoans == null) {
            return 0;
        }
        return Math.max(0, maxSimultaneousLoans - activeLoans);
    }
}
