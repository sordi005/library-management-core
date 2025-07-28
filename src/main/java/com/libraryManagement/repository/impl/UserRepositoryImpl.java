package com.libraryManagement.repository.impl;

import com.libraryManagement.config.db.TransactionUtil;
import com.libraryManagement.exception.repository.RepositoryException;
import com.libraryManagement.model.User;
import com.libraryManagement.repository.interfaces.UserRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * IMPLEMENTACIÓN PROFESIONAL DE UserRepository
 * USANDO JPA + TransactionUtil OPTIMIZADO
 *
 * Orden de métodos:
 * 1. Métodos de GenericRepository (CRUD básico)
 * 2. Métodos específicos de UserRepository (en orden de interface)
 */
public class UserRepositoryImpl implements UserRepository {

    // =====================================================================
    // MÉTODOS DE GENERIC REPOSITORY - CON JPA + TransactionUtil OPTIMIZADO
    // =====================================================================

    @Override
    public User save(User user) {
        try {
            return TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    return em.merge(user);
                } catch (Exception e) {
                    throw new RepositoryException("Error al guardar usuario", "SAVE", "User", e);
                }
            });
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado al guardar usuario", "SAVE", "User", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            User user = TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    return em.find(User.class, id);
                } catch (Exception e) {
                    throw new RepositoryException("Error consultando usuario por ID: " + id, "FIND", "User", e);
                }
            });
            return Optional.ofNullable(user);
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado consultando usuario por ID: " + id, "FIND", "User", e);
        }
    }

    @Override
    public List<User> findAll() {
        try {
            return TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    TypedQuery<User> query = em.createQuery(
                        "SELECT u FROM User u ORDER BY u.lastName, u.firstName", User.class);
                    return query.getResultList();
                } catch (Exception e) {
                    throw new RepositoryException("Error consultando todos los usuarios", "FIND_ALL", "User", e);
                }
            });
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado consultando todos los usuarios", "FIND_ALL", "User", e);
        }
    }

    @Override
    public void update(User user) {
        try {
            TransactionUtil.executeInTransaction(em -> {
                try {
                    em.merge(user);
                } catch (Exception e) {
                    throw new RepositoryException("Error al actualizar usuario", "UPDATE", "User", e);
                }
            });
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado al actualizar usuario", "UPDATE", "User", e);
        }
    }

    @Override
    public void delete(User user) {
        try {
            TransactionUtil.executeInTransaction(em -> {
                try {
                    User managedUser = em.merge(user);
                    em.remove(managedUser);
                } catch (Exception e) {
                    throw new RepositoryException("Error al eliminar usuario", "DELETE", "User", e);
                }
            });
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado al eliminar usuario", "DELETE", "User", e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        try {
            Long count = TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    TypedQuery<Long> query = em.createQuery(
                        "SELECT COUNT(u.id) FROM User u WHERE u.id = :id", Long.class);
                    query.setParameter("id", id);
                    return query.getSingleResult();
                } catch (Exception e) {
                    throw new RepositoryException("Error verificando existencia de usuario por ID: " + id, "EXISTS", "User", e);
                }
            });
            return count > 0;
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado verificando existencia por ID: " + id, "EXISTS", "User", e);
        }
    }

    // =====================================================================
    // BÚSQUEDAS ÚNICAS User
    // =====================================================================

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            User user = TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    TypedQuery<User> query = em.createQuery(
                        "SELECT u FROM User u WHERE u.email = :email", User.class);
                    query.setParameter("email", email);
                    return query.getSingleResult();
                } catch (NoResultException e) {
                    return null;
                } catch (Exception e) {
                    throw new RepositoryException("Error consultando usuario por email: " + email, "FIND_BY_EMAIL", "User", e);
                }
            });
            return Optional.ofNullable(user);
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado consultando por email: " + email, "FIND_BY_EMAIL", "User", e);
        }
    }

    @Override
    public Optional<User> findByDni(String dni) {
        try {
            User user = TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    TypedQuery<User> query = em.createQuery(
                        "SELECT u FROM User u WHERE u.dni = :dni", User.class);
                    query.setParameter("dni", dni);
                    return query.getSingleResult();
                } catch (NoResultException e) {
                    return null;
                } catch (Exception e) {
                    throw new RepositoryException("Error consultando usuario por DNI: " + dni, "FIND_BY_DNI", "User", e);
                }
            });
            return Optional.ofNullable(user);
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado consultando por DNI: " + dni, "FIND_BY_DNI", "User", e);
        }
    }

    // =====================================================================
    // VALIDACIONES DE EXISTENCIA CON JPA
    // =====================================================================

    @Override
    public boolean existsByEmail(String email) {
        try {
            Long count = TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    TypedQuery<Long> query = em.createQuery(
                        "SELECT COUNT(u.id) FROM User u WHERE u.email = :email", Long.class);
                    query.setParameter("email", email);
                    return query.getSingleResult();
                } catch (Exception e) {
                    throw new RepositoryException("Error verificando existencia de email: " + email, "EXISTS_BY_EMAIL", "User", e);
                }
            });
            return count > 0;
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado verificando email: " + email, "EXISTS_BY_EMAIL", "User", e);
        }
    }

    @Override
    public boolean existsByDni(String dni) {
        try {
            Long count = TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    TypedQuery<Long> query = em.createQuery(
                        "SELECT COUNT(u.id) FROM User u WHERE u.dni = :dni", Long.class);
                    query.setParameter("dni", dni);
                    return query.getSingleResult();
                } catch (Exception e) {
                    throw new RepositoryException("Error verificando existencia de DNI: " + dni, "EXISTS_BY_DNI", "User", e);
                }
            });
            return count > 0;
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado verificando DNI: " + dni, "EXISTS_BY_DNI", "User", e);
        }
    }

    // =====================================================================
    // BÚSQUEDAS MÚLTIPLES CON JPA
    // =====================================================================

    @Override
    public List<User> findByActiveLoans() {
        try {
            return TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    TypedQuery<User> query = em.createQuery(
                        "SELECT DISTINCT u FROM User u JOIN u.loans l WHERE l.returnedAt IS NULL", User.class);
                    return query.getResultList();
                } catch (Exception e) {
                    throw new RepositoryException("Error consultando usuarios con préstamos activos", "FIND_BY_ACTIVE_LOANS", "User", e);
                }
            });
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado consultando usuarios con préstamos activos", "FIND_BY_ACTIVE_LOANS", "User", e);
        }
    }

    @Override
    public List<User> findByOverdueLoans() {
        try {
            return TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    TypedQuery<User> query = em.createQuery(
                        "SELECT DISTINCT u FROM User u JOIN u.loans l WHERE l.returnedAt IS NULL AND l.dueDate < :today", User.class);
                    query.setParameter("today", LocalDate.now());
                    return query.getResultList();
                } catch (Exception e) {
                    throw new RepositoryException("Error consultando usuarios con préstamos vencidos", "FIND_BY_OVERDUE_LOANS", "User", e);
                }
            });
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado consultando usuarios con préstamos vencidos", "FIND_BY_OVERDUE_LOANS", "User", e);
        }
    }

    // =====================================================================
    // PAGINACIÓN CON JPA
    // =====================================================================

    @Override
    public List<User> findPaginated(int page, int size) {
        try {
            return TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    TypedQuery<User> query = em.createQuery(
                        "SELECT u FROM User u ORDER BY u.lastName, u.firstName", User.class);
                    query.setFirstResult(page * size);
                    query.setMaxResults(size);
                    return query.getResultList();
                } catch (Exception e) {
                    throw new RepositoryException("Error en consulta paginada", "FIND_PAGINATED", "User", e);
                }
            });
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado en consulta paginada", "FIND_PAGINATED", "User", e);
        }
    }

    @Override
    public List<User> searchByName(String namePattern, int page, int size) {
        try {
            return TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    TypedQuery<User> query = em.createQuery(
                        "SELECT u FROM User u WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(:pattern) ORDER BY u.lastName, u.firstName", User.class);
                    query.setParameter("pattern", "%" + namePattern + "%");
                    query.setFirstResult(page * size);
                    query.setMaxResults(size);
                    return query.getResultList();
                } catch (Exception e) {
                    throw new RepositoryException("Error buscando usuarios por nombre: " + namePattern, "SEARCH_BY_NAME", "User", e);
                }
            });
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado buscando por nombre: " + namePattern, "SEARCH_BY_NAME", "User", e);
        }
    }

    @Override
    public Long countByActiveLoans() {
        try {
            return TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    TypedQuery<Long> query = em.createQuery(
                        "SELECT COUNT(DISTINCT u.id) FROM User u JOIN u.loans l WHERE l.returnedAt IS NULL", Long.class);
                    return query.getSingleResult();
                } catch (Exception e) {
                    throw new RepositoryException("Error contando usuarios con préstamos activos", "COUNT_BY_ACTIVE_LOANS", "User", e);
                }
            });
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado contando usuarios con préstamos activos", "COUNT_BY_ACTIVE_LOANS", "User", e);
        }
    }

    // =====================================================================
    // VALIDACIONES DE NEGOCIO - Sin cargar colecciones completas
    // =====================================================================

    @Override
    public boolean hasActiveLoans(Long userId) {
        try {
            Long count = TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    TypedQuery<Long> query = em.createQuery(
                        "SELECT COUNT(l.id) FROM Loan l WHERE l.user.id = :userId AND l.returnedAt IS NULL",
                        Long.class);
                    query.setParameter("userId", userId);
                    return query.getSingleResult();
                } catch (Exception e) {
                    throw new RepositoryException("Error verificando préstamos activos del usuario: " + userId, "HAS_ACTIVE_LOANS", "User", e);
                }
            });
            return count > 0;
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado verificando préstamos activos: " + userId, "HAS_ACTIVE_LOANS", "User", e);
        }
    }

    @Override
    public Long countActiveLoansByUserId(Long userId) {
        try {
            return TransactionUtil.executeInTransactionWithResult(em -> {
                try {
                    TypedQuery<Long> query = em.createQuery(
                        "SELECT COUNT(l.id) FROM Loan l WHERE l.user.id = :userId AND l.returnedAt IS NULL",
                        Long.class);
                    query.setParameter("userId", userId);
                    return query.getSingleResult();
                } catch (Exception e) {
                    throw new RepositoryException("Error contando préstamos activos del usuario: " + userId, "COUNT_ACTIVE_LOANS", "User", e);
                }
            });
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error inesperado contando préstamos activos: " + userId, "COUNT_ACTIVE_LOANS", "User", e);
        }
    }
}
