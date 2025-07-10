package com.libraryManagement.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository <T,ID>{
    void save(T entity);
    void update(T entity);
    Optional<T> findById(ID id);
    void delete(T entity);
    List<T> findAll();
}
