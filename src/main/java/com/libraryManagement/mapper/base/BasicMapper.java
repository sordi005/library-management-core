package com.libraryManagement.mapper.base;

import java.util.Collection;
import java.util.List;

/**
 * MAPPER BÁSICO - Para entidades SIN relaciones complejas
 *
 * Ejemplos: Genre, Publisher, Address
 *
 * Características:
 * - Solo mapeo directo
 * - Sin contexto (no necesario)
 * - Máxima simplicidad y velocidad
 *
 * @param <E> Entidad
 * @param <S> SimpleDTO
 * @param <D> DetailDTO
 * @param <C> CreateDTO
 * @param <U> UpdateDTO
 */
public interface BasicMapper<E, S, D, C, U> {

    // =====================================================================
    // MAPEO BÁSICO - Solo conversión directa
    // =====================================================================

    S toSimpleDTO(E entity);
    D toDetailDTO(E entity);
    E toEntity(C createDTO);
    E updateEntity(E existingEntity, U updateDTO);

    // =====================================================================
    // LISTAS - Sin contexto
    // =====================================================================

    default List<S> toSimpleDTOList(Collection<E> entities) {
        if (entities == null) return List.of();

        return entities.stream()
            .filter(entity -> entity != null)
            .map(this::toSimpleDTO)
            .toList();
    }

    default List<D> toDetailDTOList(Collection<E> entities) {
        if (entities == null) return List.of();

        return entities.stream()
            .filter(entity -> entity != null)
            .map(this::toDetailDTO)
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
