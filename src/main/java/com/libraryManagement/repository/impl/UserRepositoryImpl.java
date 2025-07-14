package com.libraryManagement.repository.impl;

import com.libraryManagement.model.User;
import com.libraryManagement.repository.interfaces.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private final EntityManager em;

    public UserRepositoryImpl(EntityManager em) {
        this.em = em;
    }
    @Override
    public void save(User user) {
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
    }
    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }
    @Override
    public void update(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser null");
        }
        if (entity.getId() == null) {
            throw new IllegalArgumentException("No se puede actualizar una entidad sin ID");
        }
        em.merge(entity);
    }
    @Override
    public void deleteById(Long id) {
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
    }
    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }
    @Override
    public boolean existsById(Long id) {
        return em.createQuery("""
            SELECT COUNT(u) FROM User u WHERE u.id = :id
            """, Long.class)
                .setParameter("id", id)
                .getSingleResult() > 0;
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
    public boolean existsByEmail(String email) {
        return em.createQuery("""
            SELECT COUNT(u) FROM User u WHERE u.email = :email
            """, Long.class)
                .setParameter("email", email)
                .getSingleResult() > 0;
    }
    @Override
    public boolean existsByDni(String dni) {
        return em.createQuery("""
            SELECT COUNT(u) FROM User u WHERE u.dni = :dni
            """, Long.class)
                .setParameter("dni", dni)
                .getSingleResult() > 0;
    }
    @Override
    public List<User> findPaginated(int page, int size) {
        return em.createQuery("""
            SELECT u FROM User u ORDER BY u.firstName ASC,u.lastName ASC
            """, User.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

}
