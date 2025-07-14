package com.libraryManagement.mapper;

import com.libraryManagement.dto.UserDTO;
import com.libraryManagement.model.User;

public class UserMapper {
    private final AddressMapper addressMapper;  // ✅ Corregido nombre

    public UserMapper(){
        this.addressMapper = new AddressMapper();  // ✅ Agregado 'this.'
    }
    /**
     * Convierte un UserDto a entidad User.
     * @param dto DTO a convertir
     * @return Entidad User convertida
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .dni(user.getDni())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .addressDTO(addressMapper.toDTO(user.getAddress()))
                .build();
    }
    /**
     * Convierte una entidad User a UserDto.
     * @param user Entidad User a convertir
     * @return UserDto convertido
     */
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .dateOfBirth(dto.getDateOfBirth())
                .dni(dto.getDni())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(addressMapper.toEntity(dto.getAddressDTO()))
                .build();
    }
    /**
     * Actualiza una entidad User existente desde un DTO.
     * @param dto DTO con datos actualizados
     * @param user Entidad a actualizar
     */
    public void updateFromDto(UserDTO userDTO, User user) {

        // Actualizar campos básicos usando reflection o BeanUtils
        if (userDTO.getFirstName() != null) {
            user.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
        }
        if (userDTO.getDateOfBirth() != null) {
            user.setDateOfBirth(userDTO.getDateOfBirth());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }

        // Actualizar address por separado
        if (userDTO.getAddress() != null) {
            if (user.getAddress() == null) {
                user.setAddress(addressMapper.toEntity(userDTO.getAddress()));
            }

        }


}
