package com.libraryManagement.mapper.context;

/**
 * CONTEXTOS ESPECÍFICOS PARA PUBLICATION
 *
 * Simplifica el uso de contextos para publicaciones
 */
public class PublicationMappingContext {

    /**
     * Contexto para listado simple - Solo datos básicos
     *
     * USO: Catálogos, búsquedas, grillas
     * INCLUYE: Solo datos básicos de la publicación
     * PERFORMANCE: ⚡ Muy rápido
     */
    public static BaseMappingContext simple() {
        return BaseMappingContext.builder()
                .includeCalculatedFields(true) // estadísticas básicas
                .build();
    }

    /**
     * Contexto completo - Con autores, género y editorial
     *
     * USO: Vista detallada, edición, APIs completas
     * INCLUYE: Autores + género + editorial + estadísticas
     * PERFORMANCE: 🟡 Moderado (2-3 consultas extra)
     */
    public static BaseMappingContext complete() {
        return BaseMappingContext.builder()
                .includeAuthors(true)
                .includeGenre(true)
                .includePublisher(true)
                .includeCopyStats(true)
                .includeCalculatedFields(true)
                .build();
    }

    /**
     * Contexto con copias - Para gestión de inventario
     *
     * USO: Bibliotecarios, control de inventario
     * INCLUYE: Todo + copias disponibles
     * PERFORMANCE: 🔴 Costoso (múltiples consultas)
     */
    public static BaseMappingContext withCopies() {
        return BaseMappingContext.builder()
                .includeAuthors(true)
                .includeGenre(true)
                .includePublisher(true)
                .includeCopies(true)
                .includeCopyStats(true)
                .includeCalculatedFields(true)
                .build();
    }
}
