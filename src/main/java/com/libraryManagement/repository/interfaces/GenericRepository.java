package com.libraryManagement.repository.interfaces;

import java.util.List;
import java.util.Optional;

public interface GenericRepository <T,ID>{

    T save(T entity);
    Optional<T> findById(ID id);
    void update(T entity);
    void delete(T entity);
    List<T> findAll();
    boolean existsById(ID id);

}
