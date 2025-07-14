package com.libraryManagement.repository.interfaces;

import com.libraryManagement.model.User;
import com.libraryManagement.repository.GenericRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends GenericRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByDni(String dni);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
    List<User> findPaginated(int page, int size);
}