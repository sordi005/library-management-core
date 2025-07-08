package com.libraryManagement.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractPersistenceTest {
    protected static SessionFactory sessionFactory;
    protected Session session;

    @BeforeAll
    static void setUp() {
        Configuration cfg = new Configuration().configure();
        cfg.addPackage("com.libraryManagement.model");
        sessionFactory = cfg.buildSessionFactory();
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
    /**
     * Abre una nueva sesión antes de cada test.
     */
    @BeforeEach
    protected void openSession() {
        session = sessionFactory.openSession();
    }

    /**
     * Cierra la sesión después de cada test.
     */
    @AfterEach
    protected void closeSession() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }
}
