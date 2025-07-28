package com.libraryManagement.mapper.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CONTEXTO BASE PARA MAPEO DE ENTIDADES
 *
 * Contiene campos comunes que utilizan todas o la mayoría de entidades
 * para controlar qué información incluir en los mapeos DTO <-> Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseMappingContext {

    // ============= CAMPOS GENERALES (TODAS LAS ENTIDADES) =============

    /**
     * Incluir todos los detalles disponibles
     * Útil para vistas completas o administración
     */
    @Builder.Default
    private boolean includeFullDetails = false;

    /**
     * Incluir campos calculados (edad, estadísticas, etc.)
     * Útil para dashboards y reportes
     */
    @Builder.Default
    private boolean includeCalculatedFields = false;

    // ============= CAMPOS ESPECÍFICOS DE PUBLICATION =============

    /**
     * Incluir autores de la publicación (ManyToMany)
     * Performance: +1 consulta con join
     */
    @Builder.Default
    private boolean includeAuthors = false;

    /**
     * Incluir género de la publicación (ManyToOne)
     * Performance: +1 consulta simple
     */
    @Builder.Default
    private boolean includeGenre = false;

    /**
     * Incluir editorial de la publicación (ManyToOne)
     * Performance: +1 consulta simple
     */
    @Builder.Default
    private boolean includePublisher = false;

    /**
     * Incluir copias de la publicación (OneToMany)
     * Performance: +1 consulta compleja
     */
    @Builder.Default
    private boolean includeCopies = false;

    /**
     * Incluir estadísticas de copias (disponibles, prestadas, etc.)
     * Performance: +consultas adicionales para cálculos
     */
    @Builder.Default
    private boolean includeCopyStats = false;

    // ============= CAMPOS ESPECÍFICOS DE LOAN =============

    /**
     * Incluir usuario del préstamo (ManyToOne)
     * Performance: +1 consulta simple
     */
    @Builder.Default
    private boolean includeUser = false;

    /**
     * Incluir líneas de préstamo (OneToMany)
     * Performance: +1 consulta compleja
     */
    @Builder.Default
    private boolean includeLoanLines = false;

    // ============= CAMPOS ESPECÍFICOS DE COPY =============

    /**
     * Incluir publicación de la copia (ManyToOne)
     * Performance: +1 consulta simple
     */
    @Builder.Default
    private boolean includePublication = false;

    /**
     * Incluir préstamo actual de la copia (OneToOne)
     * Performance: +1 consulta simple
     */
    @Builder.Default
    private boolean includeCurrentLoan = false;

    // ============= CAMPOS ESPECÍFICOS DE USER =============

    /**
     * Incluir dirección del usuario (OneToOne)
     */
    @Builder.Default
    private boolean includeAddress = false;

    /**
     * Incluir préstamos del usuario (OneToMany)
     * Performance: +1 consulta compleja
     */
    @Builder.Default
    private boolean includeLoans = false;

    /**
     * Incluir estadísticas de préstamos calculadas
     * Performance: +consultas adicionales para cálculos
     */
    @Builder.Default
    private boolean includeLoanStats = false;

    // ============= MÉTODOS UTILITARIOS =============

    /**
     * Contexto mínimo - Solo datos básicos de la entidad
     */
    public static BaseMappingContext minimal() {
        return BaseMappingContext.builder().build();
    }

    /**
     * Contexto completo - Incluye todo
     * CUIDADO: Puede ser muy costoso en performance
     */
    public static BaseMappingContext complete() {
        return BaseMappingContext.builder()
                .includeFullDetails(true)
                .includeCalculatedFields(true)
                .includeAddress(true)
                .includeLoans(true)
                .includeLoanStats(true)
                .includeAuthors(true)
                .includeGenre(true)
                .includePublisher(true)
                .includeCopies(true)
                .includeCopyStats(true)
                .includeUser(true)
                .includeLoanLines(true)
                .includePublication(true)
                .includeCurrentLoan(true)
                .build();
    }
}
