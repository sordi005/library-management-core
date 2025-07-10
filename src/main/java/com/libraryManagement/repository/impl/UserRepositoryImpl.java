package com.libraryManagement.repository.impl;

import com.libraryManagement.model.User;
import com.libraryManagement.repository.interfaces.UserRepository;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final EntityManager em;

    public UserRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void save(User user) {

    }

    @Override
    public void update(User entity) {

    }

    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return em.createQuery("""
            SELECT u FROM User u WHERE u.email = :email
            """, User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<User> findByDni(String dni) {
        return em.createQuery("""
            SELECT u FROM User u WHERE u.dni = :dni
            """, User.class)
                .setParameter("dni", dni)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<User> findPaginated(int page, int size) {
        return em.createQuery("""
            SELECT u FROM User u ORDER BY u.lastName ASC
            """, User.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }
}
