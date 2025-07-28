package com.libraryManagement.service;

import com.libraryManagement.dto.UserDTO.CreateUserDTO;
import com.libraryManagement.dto.UserDTO.UpdateUserDTO;
import com.libraryManagement.dto.UserDTO.UserDetailDTO;
import com.libraryManagement.dto.UserDTO.UserSimpleDTO;
import com.libraryManagement.exception.service.ServiceException;
import com.libraryManagement.mapper.UserMapper;
import com.libraryManagement.mapper.context.UserMappingContext;
import com.libraryManagement.model.User;
import com.libraryManagement.repository.interfaces.UserRepository;
import com.libraryManagement.service.interfaces.UserService;
import com.libraryManagement.util.Logger;
import com.libraryManagement.validation.user.UserValidator; // ← AGREGADO: Import de UserValidator

import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        Logger.info("UserServiceImpl", "Inicializado correctamente");
    }

    @Override
    public UserDetailDTO createUser(CreateUserDTO dto) {
        Logger.startOperation("createUser", dto.getEmail(), dto.getDni());

        try {
            //  USAR UserValidator para validaciones de negocio
            if (!UserValidator.isValidEmail(dto.getEmail())) {
                Logger.warn("createUser", "Email inválido: " + dto.getEmail());
                throw new ServiceException("User", "create", "El email no tiene un formato válido");
            }

            if (!UserValidator.isValidDNI(dto.getDni())) {
                Logger.warn("createUser", "DNI inválido: " + dto.getDni());
                throw new ServiceException("User", "create", "El DNI no tiene un formato válido");
            }

            // Validaciones de duplicados
            if (userRepository.existsByDni(dto.getDni())) {
                Logger.warn("createUser", "DNI ya registrado: " + dto.getDni());
                throw new ServiceException("User", "create", "El DNI ya está registrado");
            }

            if (userRepository.existsByEmail(dto.getEmail())) {
                Logger.warn("createUser", "Email ya registrado: " + dto.getEmail());
                throw new ServiceException("User", "create", "El email ya está registrado");
            }

            //  USAR UserValidator para validar mayoría de edad si es requerido
            if (dto.getDateOfBirth() != null && !UserValidator.isAdult(dto.getDateOfBirth())) {
                Logger.warn("createUser", "Usuario menor de edad: " + dto.getDateOfBirth());
                throw new ServiceException("User", "create", "El usuario debe ser mayor de edad");
            }

            Logger.debug("Validaciones pasadas, creando usuario: " + dto.getEmail());

            User user = userMapper.toEntity(dto);
            User saved = userRepository.save(user);

            Logger.success("createUser", "Usuario creado exitosamente con ID: " + saved.getId());
            return userMapper.toDetailDTO(saved, UserMappingContext.admin());

        } catch (ServiceException e) {
            Logger.failOperation("createUser", e.getMessage());
            throw e;
        } catch (Exception e) {
            Logger.error("createUser", "Error inesperado al crear usuario", e);
            throw new ServiceException("User", "create", "Error inesperado al crear usuario", e);
        }
    }

    @Override
    public UserDetailDTO updateUser(Long id, UpdateUserDTO dto) {
        Logger.startOperation("updateUser", id, dto.getEmail());

        try {
            User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    Logger.warn("updateUser", "Usuario no encontrado con ID: " + id);
                    return new ServiceException("User", "update", "Usuario no encontrado");
                });

            Logger.debug("Usuario encontrado, actualizando: " + existingUser.getEmail());

            User updatedUser = userMapper.updateEntity(existingUser, dto);
            User saved = userRepository.save(updatedUser);

            Logger.success("updateUser", "Usuario actualizado exitosamente: " + saved.getId());
            return userMapper.toDetailDTO(saved, UserMappingContext.admin());

        } catch (ServiceException e) {
            Logger.failOperation("updateUser", e.getMessage());
            throw e;
        } catch (Exception e) {
            Logger.error("updateUser", "Error inesperado al actualizar usuario", e);
            throw new ServiceException("User", "update", "Error inesperado al actualizar usuario", e);
        }
    }

    @Override
    public UserDetailDTO findUserById(Long id) {
        Logger.startOperation("findUserById", id);

        try {
            User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    Logger.warn("findUserById", "Usuario no encontrado con ID: " + id);
                    return new ServiceException("User", "getById", "Usuario no encontrado");
                });

            Logger.debug("Usuario encontrado: " + user.getEmail());
            Logger.endOperation("findUserById");

            // 🔧 SOLUCIÓN: Usar contexto sin loans para evitar LazyInitializationException
            return userMapper.toDetailDTO(user, UserMappingContext.withAddress());

        } catch (ServiceException e) {
            Logger.failOperation("findUserById", e.getMessage());
            throw e;
        } catch (Exception e) {
            Logger.error("findUserById", "Error inesperado al obtener usuario", e);
            throw new ServiceException("User", "getById", "Error inesperado al obtener usuario", e);
        }
    }

    @Override
    public List<UserSimpleDTO> findAllUsers() {
        Logger.startOperation("findAllUsers");

        try {
            List<User> users = userRepository.findAll();
            Logger.info("findAllUsers", "Encontrados " + users.size() + " usuarios");

            List<UserSimpleDTO> result = users.stream()
                .map(user -> userMapper.toSimpleDTO(user, UserMappingContext.simple()))
                .collect(Collectors.toList());

            Logger.endOperation("findAllUsers");
            return result;

        } catch (Exception e) {
            Logger.error("findAllUsers", "Error inesperado al listar usuarios", e);
            throw new ServiceException("User", "findAll", "Error inesperado al listar usuarios", e);
        }
    }

    @Override
    public void deleteUser(Long id) {
        Logger.startOperation("deleteUser", id);

        try {
            User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    Logger.warn("deleteUser", "Usuario no encontrado con ID: " + id);
                    return new ServiceException("User", "delete", "Usuario no encontrado");
                });

            // Validar préstamos activos usando consulta JPA directa
            //previene LazyInitializationException al no cargar la colección lazy
            if (userRepository.hasActiveLoans(id)) {
                Logger.warn("deleteUser", "Usuario tiene préstamos activos, no se puede eliminar: " + id);
                throw new ServiceException("User", "delete", "No se puede eliminar un usuario con préstamos activos");
            }

            userRepository.delete(user);
            Logger.success("deleteUser", "Usuario eliminado exitosamente: " + id);

        } catch (ServiceException e) {
            Logger.failOperation("deleteUser", e.getMessage());
            throw e;
        } catch (Exception e) {
            Logger.error("deleteUser", "Error inesperado al eliminar usuario", e);
            throw new ServiceException("User", "delete", "Error inesperado al eliminar usuario: " + e.getMessage(), e);
        }
    }
}
