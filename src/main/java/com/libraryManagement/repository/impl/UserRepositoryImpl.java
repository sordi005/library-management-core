package com.libraryManagement.repository.impl;

import com.libraryManagement.model.User;
import com.libraryManagement.repository.interfaces.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * IMPLEMENTACIÓN COMPLETA DE UserRepository
 *
 * Orden de métodos:
 * 1. Métodos de GenericRepository (CRUD básico)
 * 2. Métodos específicos de UserRepository (en orden de interface)
 */
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // =====================================================================
    // MÉTODOS DE GENERIC REPOSITORY - CRUD BÁSICO
    // =====================================================================

    @Override
    public User save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("FROM User u ORDER BY u.lastName, u.firstName", User.class);
        return query.getResultList();
    }

    @Override
    public void update(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.update(user);
    }

    @Override
    public void delete(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(user);
    }

    @Override
    public boolean existsById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery("SELECT COUNT(u.id) FROM User u WHERE u.id = :id", Long.class);
        query.setParameter("id", id);
        return query.uniqueResult() > 0;
    }

    // =====================================================================
    // BÚSQUEDAS ÚNICAS - Interface específica
    // =====================================================================

    @Override
    public Optional<User> findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.uniqueResultOptional();
    }

    @Override
    public Optional<User> findByDni(String dni) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("FROM User u WHERE u.dni = :dni", User.class);
        query.setParameter("dni", dni);
        return query.uniqueResultOptional();
    }

    // =====================================================================
    // VALIDACIONES DE EXISTENCIA
    // =====================================================================

    @Override
    public boolean existsByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery("SELECT COUNT(u.id) FROM User u WHERE u.email = :email", Long.class);
        query.setParameter("email", email);
        return query.uniqueResult() > 0;
    }

    @Override
    public boolean existsByDni(String dni) {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery("SELECT COUNT(u.id) FROM User u WHERE u.dni = :dni", Long.class);
        query.setParameter("dni", dni);
        return query.uniqueResult() > 0;
    }

    // =====================================================================
    // BÚSQUEDAS MÚLTIPLES
    // =====================================================================
    //Lista de usuarios con préstamos activos
    @Override
    public List<User> findByActiveLoans() {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery(
            "SELECT DISTINCT u FROM User u JOIN u.loans l WHERE l.returnedAt IS NULL ORDER BY u.lastName, u.firstName",
            User.class
        );
        return query.getResultList();
    }
    //Lista de usuarios con préstamos vencidos
    @Override
    public List<User> findByOverdueLoans() {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery(
            "SELECT DISTINCT u FROM User u JOIN u.loans l WHERE l.returnedAt IS NULL AND l.dueDate < CURRENT_DATE ORDER BY u.lastName, u.firstName",
            User.class
        );
        return query.getResultList();
    }


    // =====================================================================
    // PAGINACIÓN BÁSICA
    // =====================================================================

    @Override
    public List<User> findPaginated(int page, int size) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("FROM User u ORDER BY u.lastName, u.firstName", User.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public List<User> searchByName(String namePattern, int page, int size) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery(
            "FROM User u WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(:pattern) ORDER BY u.lastName, u.firstName",
            User.class
        );
        query.setParameter("pattern", "%" + namePattern + "%");
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    // =====================================================================
    // MÉTODOS DE CONTEO BÁSICOS - Para paginación
    // =====================================================================
    public Long countByActiveLoans() {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery(
            "SELECT COUNT(DISTINCT u.id) FROM User u JOIN u.loans l WHERE l.returnedAt IS NULL",
            Long.class
        );
        return query.uniqueResult();
    }
}
