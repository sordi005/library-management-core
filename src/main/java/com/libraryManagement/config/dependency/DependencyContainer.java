package com.libraryManagement.config.dependency;

import com.libraryManagement.repository.impl.UserRepositoryImpl;
import com.libraryManagement.repository.interfaces.UserRepository;
import com.libraryManagement.service.UserServiceImpl;
import com.libraryManagement.service.interfaces.UserService;
import com.libraryManagement.controller.UserController;
import com.libraryManagement.mapper.UserMapper;
import com.libraryManagement.mapper.factory.MapperFactory;
import com.libraryManagement.util.Logger; // ← AGREGADO: Logger profesional

import java.util.HashMap;
import java.util.Map;

/**
 * CONTENEDOR DE DEPENDENCIAS ESCALABLE
 * Estructura profesional que soporta múltiples entidades sin explotar en complejidad
 */
public class DependencyContainer {

    // =====================================================================
    // ALMACENAMIENTO GENÉRICO POR TIPO
    // =====================================================================

    private final Map<Class<?>, Object> repositories = new HashMap<>();
    private final Map<Class<?>, Object> services = new HashMap<>();
    private final Map<Class<?>, Object> mappers = new HashMap<>();
    private final Map<Class<?>, Object> controllers = new HashMap<>();

    private MapperFactory mapperFactory;

    // =====================================================================
    // INICIALIZACIÓN MODULAR
    // =====================================================================

    public void initializeDependencies() {
        // 1. Inicializar Factory de Mappers
        initializeMapperFactory();

        // 2. Inicializar por módulos
        initializeUserModule();
        // initializeBookModule();    // Futuro
        // initializeLoanModule();    // Futuro

        Logger.success("DependencyContainer", "Dependencias inicializadas correctamente");
    }

    private void initializeMapperFactory() {
        Logger.info("DependencyContainer", "Inicializando mappers...");
        mapperFactory = MapperFactory.getInstance();
        Logger.success("DependencyContainer", "Todos los mappers inicializados");
    }

    /**
     * MÓDULO USER - Agrupa todas las dependencias de User
     */
    private void initializeUserModule() {
        // Mappers
        var userMapper = mapperFactory.getMapper(UserMapper.class);
        mappers.put(UserMapper.class, userMapper);

        // Repositories
        var userRepository = new UserRepositoryImpl();
        repositories.put(UserRepository.class, userRepository);

        // Services - ✅ DECLARAR COMO INTERFACE
        UserService userService = new UserServiceImpl((UserRepository) userRepository,
            userMapper
        );
        services.put(UserService.class, userService);

        // Controllers
        var userController = new UserController(userService);
        controllers.put(UserController.class, userController);

        Logger.success("DependencyContainer", "User module initialized");
    }

    // =====================================================================
    // ACCESO GENÉRICO Y TIPADO
    // =====================================================================

    @SuppressWarnings("unchecked")
    public <T> T getRepository(Class<T> repositoryClass) {
        return (T) repositories.get(repositoryClass);
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }

    @SuppressWarnings("unchecked")
    public <T> T getController(Class<T> controllerClass) {
        return (T) controllers.get(controllerClass);
    }

    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> mapperClass) {
        return (T) mappers.get(mapperClass);
    }

    public MapperFactory getMapperFactory() {
        return mapperFactory;
    }

    public void shutdown() {
        repositories.clear();
        services.clear();
        mappers.clear();
        controllers.clear();
        Logger.info("DependencyContainer", "Dependencias liberadas");
    }
}
