package com.libraryManagement.model;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractPersistenceTest {
    protected static SessionFactory sessionFactory;

    @BeforeAll
    static void setUp() {
        Configuration cfg = new Configuration().configure();
        cfg.addAnnotatedClass(Book.class);
        cfg.addAnnotatedClass(Loan.class);
        // ... otras entidades
        sessionFactory = cfg.buildSessionFactory();
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}