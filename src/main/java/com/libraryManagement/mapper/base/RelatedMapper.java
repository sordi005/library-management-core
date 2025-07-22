package com.libraryManagement.mapper.base;

import com.libraryManagement.mapper.context.MappingContext;
import java.util.Collection;
import java.util.List;

/**
 * MAPPER RELACIONAL COMPLETO - Para entidades CON relaciones complejas
 *
 * Ejemplos: User, Publication, Loan
 *
 * @param <E> Entidad
 * @param <S> SimpleDTO
 * @param <D> DetailDTO
 * @param <C> CreateDTO
 * @param <U> UpdateDTO
 */
public interface RelatedMapper<E, S, D, C, U> {

    // =====================================================================
    // MAPEO DE LECTURA - CON CONTEXTO
    // =====================================================================

    /**
     * Mapeo simple con contexto por defecto
     */
    default S toSimpleDTO(E entity) {
        return toSimpleDTO(entity, MappingContext.minimal());
    }

    /**
     * Mapeo detallado con contexto por defecto
     */
    default D toDetailDTO(E entity) {
        return toDetailDTO(entity, MappingContext.minimal());
    }

    /**
     * Mapeo simple con contexto específico
     */
    S toSimpleDTO(E entity, MappingContext context);

    /**
     * Mapeo detallado con contexto específico
     */
    D toDetailDTO(E entity, MappingContext context);

    // =====================================================================
    // MAPEO DE ESCRITURA - ¡AHORA SÍ ESTÁN!
    // =====================================================================

    /**
     * Convierte CreateDTO a nueva entidad
     */
    E toEntity(C createDTO);

    /**
     * Actualiza entidad existente con UpdateDTO
     */
    E updateEntity(E existingEntity, U updateDTO);

    // =====================================================================
    // MAPEO DE LISTAS
    // =====================================================================

    default List<S> toSimpleDTOList(Collection<E> entities) {
        return toSimpleDTOList(entities, MappingContext.minimal());
    }

    default List<S> toSimpleDTOList(Collection<E> entities, MappingContext context) {
        if (entities == null) return List.of();

        return entities.stream()
                .filter(entity -> entity != null)
                .map(entity -> toSimpleDTO(entity, context))
                .toList();
    }

    default List<D> toDetailDTOList(Collection<E> entities) {
        return toDetailDTOList(entities, MappingContext.minimal());
    }

    default List<D> toDetailDTOList(Collection<E> entities, MappingContext context) {
        if (entities == null) return List.of();

        return entities.stream()
                .filter(entity -> entity != null)
                .map(entity -> toDetailDTO(entity, context))
                .toList();
    }

    // =====================================================================
    // UTILIDADES
    // =====================================================================

    default boolean isNull(Object obj) {
        return obj == null;
    }

    default boolean isNotNull(Object obj) {
        return obj != null;
    }
}
