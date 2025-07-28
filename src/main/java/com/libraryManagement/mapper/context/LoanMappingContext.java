package com.libraryManagement.mapper.context;

/**
 * CONTEXTOS ESPECÍFICOS PARA LOAN
 *
 * Simplifica el uso de contextos para préstamos
 */
public class LoanMappingContext {

    /**
     * Contexto para listado simple - Solo datos básicos del préstamo
     *
     * USO: Listados de préstamos, reportes básicos
     * INCLUYE: Solo datos del préstamo (fechas, estado, etc.)
     * PERFORMANCE: ⚡ Muy rápido
     */
    public static BaseMappingContext simple() {
        return BaseMappingContext.builder()
                .includeCalculatedFields(true) // días de retraso, etc.
                .build();
    }

    /**
     * Contexto completo - Con usuario y líneas de préstamo
     *
     * USO: Vista detallada, gestión bibliotecaria
     * INCLUYE: Usuario + líneas de préstamo + cálculos
     * PERFORMANCE: 🟡 Moderado (1-2 consultas extra)
     */
    public static BaseMappingContext complete() {
        return BaseMappingContext.builder()
                .includeUser(true)
                .includeLoanLines(true)
                .includeCalculatedFields(true)
                .build();
    }
}
