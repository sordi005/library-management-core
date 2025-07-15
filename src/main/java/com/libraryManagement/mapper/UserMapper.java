package com.libraryManagement.mapper;

import com.libraryManagement.dto.UserDTO;
import com.libraryManagement.model.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(
        componentModel = "default",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AddressMapper.class}  // ✅ Corregido paréntesis
)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * DTO MAPPERS
     */
    @Mapping(target = "loanCount", expression = "java(user.getLoans().size())")
    @Mapping(target = "addressDTO", source = "address")
    UserDTO toDTO(User user);

    // Para listas y referencias (masivo)
    @Mapping(target = "loanCount", ignore = true)
    UserDTO toBasicDTO(User user);

    //Convierte una lista de entidades a una lista de DTOs
    List<UserDTO> toDTOs(List<User> users);

    // Convierte una lista de entidades a una lista de DTOs básicos (sin conteo de préstamos)
    List<UserDTO> toBasicDTOs(List<User> users);

    /**
     * ENTITY MAPPERS
     */
    // DTO -> Entity (para creación)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "loans", ignore = true)
    @Mapping(target = "loanCount", ignore = true)  // ✅ OBLIGATORIO
    @Mapping(target = "address", source = "addressDTO")
    User toEntity(UserDTO userDTO);

    //Método para actualizar entidad existente
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dni", ignore = true)  // ✅ Excelente - DNI inmutable
    @Mapping(target = "loans", ignore = true)
    @Mapping(target = "loanCount", ignore = true)
    @Mapping(target = "address", source = "addressDTO")
    void updateEntityFromDTO(UserDTO userDTO, @MappingTarget User user);





}