package com.libraryManagement.mapper;

import com.libraryManagement.dto.AddressDTO.AddressSimpleDTO;
import com.libraryManagement.dto.AddressDTO.CreateAddressDTO;
import com.libraryManagement.dto.AddressDTO.UpdateAddressDTO;
import com.libraryManagement.dto.UserDTO.CreateUserDTO;
import com.libraryManagement.dto.UserDTO.UpdateUserDTO;
import com.libraryManagement.dto.UserDTO.UserDetailDTO;
import com.libraryManagement.dto.UserDTO.UserSimpleDTO;
import com.libraryManagement.mapper.base.RelatedMapper;
import com.libraryManagement.mapper.factory.MapperFactory;
import com.libraryManagement.mapper.base.BasicMapper;
import com.libraryManagement.mapper.context.BaseMappingContext;
import com.libraryManagement.model.User;
import com.libraryManagement.model.Address;
import com.libraryManagement.domain.user.UserUtils; // CAMBIADO: Ahora desde domain/user

import java.util.*;
import java.util.stream.Collectors;

/**
 * MAPPER SIMPLIFICADO PARA USER
 * Usa SimpleMapper que combina lo mejor de todos los mappers
 * Sin sobreingeniería, máximo poder
 */
public class UserMapper implements RelatedMapper<User, UserSimpleDTO, UserDetailDTO, CreateUserDTO, UpdateUserDTO> {

    // =====================================================================
    // DEPENDENCIAS - Inyectadas desde el Service
    // =====================================================================

    private final AddressMapper addressMapper;

    /**
     * Constructor con inyección de dependencias
     * ¿Por qué el mapper tiene repository?
     * - Para casos donde necesita verificar datos durante el mapeo
     * - Para calcular campos complejos que requieren consultas
     * - Principio: El mapper puede LEER pero NO ESCRIBIR en BD
     */
    public UserMapper( AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    // =====================================================================
    // MAPEO BÁSICO - Entity → SimpleDTO
    // =====================================================================
    /**
     * Convierte User → UserSimpleDTO
     * CASO DE USO: Listados, búsquedas, grillas
     * CARACTERÍSTICA: Rápido, solo datos esenciales
     */
    @Override
    public UserSimpleDTO toSimpleDTO(User entity, BaseMappingContext context) {
        //  VALIDACIÓN: Siempre validar entrada
        if (isNull(entity)) {
            return null;
        }

        var builder = UserSimpleDTO.builder()
            .id(entity.getId())
            .fullName(UserUtils.formatFullName(entity.getFirstName(), entity.getLastName()))
            .dateOfBirth(entity.getDateOfBirth())
            .dni(entity.getDni())
            .email(entity.getEmail())
            .phoneNumber(entity.getPhoneNumber());

        //  CAMPOS CALCULADOS: Solo si el contexto lo permite
        if (context != null && context.isIncludeCalculatedFields()) {
            builder.age(UserUtils.calculateAge(entity.getDateOfBirth()));
        }

        //  DATOS DE DIRECCIÓN: Solo cityName si incluye address
        if (context != null && context.isIncludeAddress()) {
            builder.cityName(UserUtils.extractCityName(entity.getAddress()));
        }

        //  DATOS DE PRÉSTAMOS: Solo si incluye loans
        if (context != null && context.isIncludeLoans()) {
            builder.loanActive(entity.hasActiveLoans());
        }

        return builder.build();
    }

    // =====================================================================
    // MAPEO DETALLADO - Entity → DetailDTO
    // =====================================================================

    /**
     * Convierte User → UserDetailDTO
     * CASO DE USO: Vista de perfil, edición, detalles
     * CARACTERÍSTICA: Completo, incluye relaciones y estadísticas
     */
    @Override
    public UserDetailDTO toDetailDTO(User entity, BaseMappingContext context) {

        if (isNull(entity)) {
            return null;
        }

        var builder = UserDetailDTO.builder()
            .id(entity.getId())
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .dateOfBirth(entity.getDateOfBirth())
            .dni(entity.getDni())
            .email(entity.getEmail())
            .phoneNumber(entity.getPhoneNumber());

        //  CAMPOS CALCULADOS: Solo si el contexto lo permite
        if (context != null && context.isIncludeCalculatedFields()) {
            builder.fullName(UserUtils.formatFullName(entity.getFirstName(), entity.getLastName())) // ✅ USAR UserUtils
                   .age(UserUtils.calculateAge(entity.getDateOfBirth())); // ✅ USAR UserUtils
        }

        //  DATOS DE DIRECCIÓN: Incluir address y cityName si está habilitado
        if (context != null && context.isIncludeAddress() && entity.getAddress() != null) {
            builder.address(addressMapper.toSimpleDTO(entity.getAddress()))
                   .cityName(UserUtils.extractCityName(entity.getAddress())); // ✅ USAR UserUtils
        }

        //  DATOS DE PRÉSTAMOS: Incluir loans y estadísticas si está habilitado
        if (context != null && context.isIncludeLoans()) {
            // utiliza metodo tremporla antesd de al creacion de loanMapper
            builder.loans(mapLoansToSimpleDTO(entity.getLoans()))
                   .activeLoanCount(entity.getActiveLoanCount()) // USA MÉTODO DE LA ENTIDAD
                   .loanActive(entity.hasActiveLoans()); //  USA MÉTODO DE LA ENTIDAD
        }

        return builder.build();
    }

    // =====================================================================
    // MAPEO DE CREACIÓN - CreateDTO → Entity
    // =====================================================================

    /**
     * Convierte CreateDTO → User (nueva entidad)
     *
     * PRINCIPIOS:
     * - Usa AddressMapper para mapear la relación
     * - Solo mapea campos propios de User
     * - NO setea ID (lo genera la BD)
     */
    @Override
    public User toEntity(CreateUserDTO UserCreateDTO) {
        if (isNull(UserCreateDTO)) {
            return null;
        }

        // CONSTRUCCIÓN DE ENTIDAD: Solo campos básicos
        var userBuilder = User.builder()
            .firstName(UserCreateDTO.getFirstName())
            .lastName(UserCreateDTO.getLastName())
            .dni(UserCreateDTO.getDni())
            .email(UserCreateDTO.getEmail())
            .phoneNumber(UserCreateDTO.getPhoneNumber())
            .dateOfBirth(UserCreateDTO.getDateOfBirth());

        // MAPEO DE DIRECCIÓN: Usar AddressMapper dedicado
        if (UserCreateDTO.getAddress() != null) {
            // Usar el AddressMapper para convertir CreateAddressDTO → Address
            var address = addressMapper.toEntity(UserCreateDTO.getAddress());
            userBuilder.address(address);
        }

        return userBuilder.build();
    }

    // =====================================================================
    // MAPEO DE ACTUALIZACIÓN - UpdateDTO → Entity existente
    // =====================================================================

    /**
     * Actualiza entidad existente con datos del UpdateUserDTO
     */
    @Override
    public User updateEntity(User userEntity, UpdateUserDTO updateDTO) {
        if (isNull(userEntity) || isNull(updateDTO)) {
            return userEntity;
        }

        if (updateDTO.getFirstName() != null) {
            userEntity.setFirstName(updateDTO.getFirstName());
        }

        if (updateDTO.getLastName() != null) {
            userEntity.setLastName(updateDTO.getLastName());
        }

        if (updateDTO.getEmail() != null) {
            userEntity.setEmail(updateDTO.getEmail());
        }

        if (updateDTO.getPhoneNumber() != null) {
            userEntity.setPhoneNumber(updateDTO.getPhoneNumber());
        }

        if (updateDTO.getDateOfBirth() != null) {
            userEntity.setDateOfBirth(updateDTO.getDateOfBirth());
        }

        // ACTUALIZACIÓN DE DIRECCIÓN: Usar AddressMapper correctamente
        if (updateDTO.getAddress() != null) {
            if (userEntity.getAddress() != null) {
                //Actualizar dirección existente y asignar el resultado
                Address updatedAddress = addressMapper.updateEntity(userEntity.getAddress(), updateDTO.getAddress());
                userEntity.setAddress(updatedAddress);
            } else {
                // Crear nueva dirección si no existía
                var newAddressEntity = addressMapper.toEntity(updateDTO.getAddress());
                userEntity.setAddress(newAddressEntity);
            }
        }

        //  DNI es inmutable - no se actualiza nunca

        return userEntity;
    }

    // =====================================================================
    // MAPEO DE COLECCIONES - Para operaciones masivas
    // =====================================================================

    @Override
    public List<UserSimpleDTO> toSimpleDTOList(Collection<User> entities) {
        return safeList(entities).stream()
            .map(this::toSimpleDTO)
            .filter(Objects::nonNull) // Filtrar nulls por seguridad
            .collect(Collectors.toList());
    }

    @Override
    public List<UserDetailDTO> toDetailDTOList(Collection<User> entities) {
        return safeList(entities).stream()
            .map(this::toDetailDTO)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    // =====================================================================
    // MAPEO DE RELACIONES - Para objetos relacionados
    // =====================================================================

    /**
     * Mapea conjunto de Loans a DTOs simples
     * MANEJO PROFESIONAL DE COLECCIONES:
     *  Validación de nulls
     *  Colecciones vacías
     *  Filtrado de elementos inválidos
     *  Preservar tipo de colección (Set)
     */
    private Set<com.libraryManagement.dto.LoanDTO.LoanSimpleDTO> mapLoansToSimpleDTO(Set<com.libraryManagement.model.Loan> loans) {

        //  VALIDACIÓN: Manejar colecciones null o vacías
        if (loans == null || loans.isEmpty()) {
            return new HashSet<>(); // Retornar Set vacío, nunca null
        }

        // MAPEO CON STREAMS: Transformar Loan → LoanSimpleDTO
        return loans.stream()
            .filter(Objects::nonNull)           // Filtrar loans null
            .map(this::mapLoanToSimpleDTO)      // Transformar cada loan
            .filter(Objects::nonNull)           // Filtrar DTOs null (por si falla el mapeo)
            .collect(Collectors.toSet());       // Recolectar como Set (no List)
    }

    /**
     * MAPEO INDIVIDUAL: Loan → LoanSimpleDTO
     * IMPLEMENTACIÓN v: Hasta crear LoanMapper dedicado
     * TODO: Reemplazar por loanMapper.toSimpleDTO() cuando esté listo
     */
    private com.libraryManagement.dto.LoanDTO.LoanSimpleDTO mapLoanToSimpleDTO(
            com.libraryManagement.model.Loan loan) {

        if (loan == null) {
            return null;
        }

        // MAPEO MANUAL TEMPORAL - Solo campos esenciales
        // En producción: return loanMapper.toSimpleDTO(loan);
        return com.libraryManagement.dto.LoanDTO.LoanSimpleDTO.builder()
            .id(loan.getId())
            .userFullName(loan.getUser().getFullName())
            .startDate(loan.getStartDate())
            .dueDate(loan.getDueDate())
            .returnAt(loan.getReturnedAt())
            .build();
    }

    // =====================================================================
    // MÉTODOS DE UTILIDAD - Helpers comunes
    // =====================================================================

    /**
     * Verifica si un objeto es null
     * PATRÓN: Método de utilidad para legibilidad
     */
    public boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * Convierte Collection a List segura
     * PROPÓSITO: Evitar NullPointerException en streams
     */
    private <T> List<T> safeList(Collection<T> collection) {
        return collection != null ? new ArrayList<>(collection) : new ArrayList<>();
    }
}
