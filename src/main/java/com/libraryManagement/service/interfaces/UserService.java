package com.libraryManagement.service.interfaces;

import com.libraryManagement.dto.UserDTO.CreateUserDTO;
import com.libraryManagement.dto.UserDTO.UpdateUserDTO;
import com.libraryManagement.dto.UserDTO.UserDetailDTO;
import com.libraryManagement.dto.UserDTO.UserSimpleDTO;

import java.util.List;

/**
 * Interfaz para la capa de servicios de User
 * Define las operaciones principales de negocio para usuarios
 */
public interface UserService {
    UserDetailDTO createUser(CreateUserDTO dto);
    UserDetailDTO updateUser(Long id, UpdateUserDTO dto);
    UserDetailDTO findUserById(Long id);
    List<UserSimpleDTO> findAllUsers();
    void deleteUser(Long id);
    // Puedes agregar más métodos según necesidades de negocio
}

