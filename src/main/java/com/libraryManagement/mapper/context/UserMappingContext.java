package com.libraryManagement.mapper.context;

import lombok.Builder;
import lombok.Data;

/**
 * CONTEXTO ESPECÍFICO PARA USER
 * Utiliza el BaseMappingContext existente como base
 */
@Data
@Builder
public class UserMappingContext {

    // =====================================================================
    // FACTORY METHODS - CONTEXTOS PREDEFINIDOS PARA USER
    // =====================================================================

    /**
     * Contexto simple - solo datos básicos sin relaciones
     */
    public static BaseMappingContext simple() {
        return BaseMappingContext.builder()
                .includeCalculatedFields(false)
                .includeAddress(false)
                .includeLoans(false)
                .includeLoanStats(false)
                .includeFullDetails(false)
                .build();
    }

    public static BaseMappingContext basic() {
        return BaseMappingContext.builder()
                .includeCalculatedFields(false)
                .includeAddress(false)
                .includeLoans(false)
                .build();
    }

    public static BaseMappingContext withAddress() {
        return BaseMappingContext.builder()
                .includeCalculatedFields(true)
                .includeAddress(true)
                .includeLoans(false)
                .build();
    }

    public static BaseMappingContext profile() {
        return BaseMappingContext.builder()
                .includeCalculatedFields(true)
                .includeAddress(true)
                .includeLoanStats(true)
                .includeLoans(false)
                .build();
    }

    public static BaseMappingContext admin() {
        return BaseMappingContext.builder()
                .includeCalculatedFields(true)
                .includeAddress(true)
                .includeLoans(true)
                .includeLoanStats(true)
                .includeFullDetails(true)
                .build();
    }
}
