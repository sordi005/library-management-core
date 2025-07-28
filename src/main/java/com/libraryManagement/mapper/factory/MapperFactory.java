package com.libraryManagement.mapper.factory;

import com.libraryManagement.mapper.AddressMapper;
import com.libraryManagement.mapper.UserMapper;
import com.libraryManagement.util.Logger; // ← AGREGADO: Logger profesional

import java.util.HashMap;
import java.util.Map;

/**
 * FACTORY SIMPLIFICADO PARA MAPPERS
 *
 * Responsabilidad: Crear y gestionar instancias de mappers
 *
 * FLUJO DE INICIALIZACIÓN:
 * 1. Crear mappers simples (sin dependencias)
 * 2. Crear mappers avanzados (con dependencias)
 */
public class MapperFactory {

    private static MapperFactory instance;
    private final Map<Class<?>, Object> mappers = new HashMap<>();
    private boolean initialized = false;

    // Singleton
    private MapperFactory() {}

    public static MapperFactory getInstance() {
        if (instance == null) {
            instance = new MapperFactory();
        }
        return instance;
    }

    /**
     * Inicializa TODOS los mappers de forma segura
     */
    public void initializeMappers() {
        if (initialized) {
            return; // ✅ Evitar doble inicialización
        }

        Logger.info("MapperFactory", "Inicializando mappers...");

        // 1. Mappers simples PRIMERO (sin dependencias)
        initializeSimpleMappers();

        // 2. Mappers avanzados DESPUÉS (con dependencias ya resueltas)
        initializeAdvancedMappers();

        initialized = true;
        Logger.success("MapperFactory", "Todos los mappers inicializados");
    }

    /**
     * Mappers que NO tienen dependencias de otros mappers
     */
    private void initializeSimpleMappers() {
        // AddressMapper es simple, no depende de otros mappers
        mappers.put(AddressMapper.class, new AddressMapper());
        Logger.debug("MapperFactory", "Simple mappers initialized");
    }

    /**
     * Mappers que SÍ dependen de otros mappers
     */
    private void initializeAdvancedMappers() {
        // ✅ ACCESO DIRECTO al mapper ya creado
        AddressMapper addressMapper = (AddressMapper) mappers.get(AddressMapper.class);

        // UserMapper depende de AddressMapper
        UserMapper userMapper = new UserMapper(addressMapper);
        mappers.put(UserMapper.class, userMapper);

        Logger.debug("MapperFactory", "Advanced mappers initialized");
    }

    /**
     * Obtiene un mapper específico por su clase
     */
    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> mapperClass) {
        // ✅ Inicializar solo si no está inicializado
        if (!initialized) {
            initializeMappers();
        }

        T mapper = (T) mappers.get(mapperClass);

        if (mapper == null) {
            // ERROR: Mapper no encontrado
            Logger.error("MapperFactory: Mapper no encontrado: " + mapperClass.getSimpleName() +
            ". Mappers disponibles: " + mappers.keySet());
            throw new IllegalArgumentException(
                " Mapper no encontrado: " + mapperClass.getSimpleName() +
                ". Mappers disponibles: " + mappers.keySet()
            );
        }

        return mapper;
    }

    /**
     * Obtiene UserMapper de forma directa
     */
    public UserMapper getUserMapper() {
        return getMapper(UserMapper.class);
    }

    /**
     * Obtiene AddressMapper de forma directa
     */
    public AddressMapper getAddressMapper() {
        return getMapper(AddressMapper.class);
    }

    /**
     * Resetea el factory (útil para testing)
     */
    public void reset() {
        mappers.clear();
        initialized = false;
    }
}
