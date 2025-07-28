package com.libraryManagement;

import com.libraryManagement.config.ApplicationBootstrap;

/**
 * Punto de entrada principal de la aplicación
 * Responsabilidad: Solo iniciar el bootstrap
 */
public class LibraryManagementApplication {

    public static void main(String[] args) {
        ApplicationBootstrap bootstrap = new ApplicationBootstrap();
        bootstrap.start();
    }
}
