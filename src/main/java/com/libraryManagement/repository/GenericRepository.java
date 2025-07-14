package com.libraryManagement.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository <T,ID>{

    void save(T entity);
    Optional<T> findById(ID id);
    void update(T entity);
    void deleteById(ID id);
    List<T> findAll();
    boolean existsById(ID id);

}
