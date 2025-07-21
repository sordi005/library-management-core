package com.libraryManagement.repository.interfaces;

import com.libraryManagement.model.User;
import com.libraryManagement.repository.GenericRepository;
import com.libraryManagement.dto.statistics.LoanStatsDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends GenericRepository<User, Long> {

    // =====================================================================
    // BÚSQUEDAS ÚNICAS - Usan Optional porque pueden no existir
    // =====================================================================

    Optional<User> findByEmail(String email);
    Optional<User> findByDni(String dni);

    // =====================================================================
    // VALIDACIONES DE EXISTENCIA - Para validaciones rápidas
    // =====================================================================

    boolean existsByEmail(String email);
    boolean existsByDni(String dni);

    // =====================================================================
    // BÚSQUEDAS MÚLTIPLES
    // =====================================================================

    List<User> findByActiveLoans();
    List<User> findByOverdueLoans();

    // =====================================================================
    // PAGINACIÓN - Para todas las búsquedas masivas
    // =====================================================================

    List<User> findPaginated(int page, int size);
    List<User> searchByName(String namePattern, int page, int size);


    Long countByActiveLoans();

}