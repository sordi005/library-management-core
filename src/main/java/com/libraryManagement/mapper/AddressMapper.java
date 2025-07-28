package com.libraryManagement.mapper;

import com.libraryManagement.mapper.base.BasicMapper;
import com.libraryManagement.model.Address;
import com.libraryManagement.dto.AddressDTO.AddressSimpleDTO;
import com.libraryManagement.dto.AddressDTO.AddressDetailDTO;
import com.libraryManagement.dto.AddressDTO.CreateAddressDTO;
import com.libraryManagement.dto.AddressDTO.UpdateAddressDTO;

/*
 * MAPPER BÁSICO PARA ADDRESS
 * Address es una entidad simple sin relaciones complejas,
 * por eso usa BasicMapper (sin contextos)
 */
public class AddressMapper implements BasicMapper<Address, AddressSimpleDTO, AddressDetailDTO, CreateAddressDTO, UpdateAddressDTO> {

    // =====================================================================
    // MAPEO BÁSICO - Entity → DTOs
    // =====================================================================

    @Override
    public AddressSimpleDTO toSimpleDTO(Address entity) {
        if (isNull(entity)) {
            return null;
        }

        return AddressSimpleDTO.builder()
                .id(entity.getId())
                .street(entity.getStreet())
                .number(entity.getNumber())
                .city(entity.getCity())
                .province(entity.getProvince())
                .build();
    }
    @Override
    public AddressDetailDTO toDetailDTO(Address entity) {
        if (isNull(entity)) {
            return null;
        }

        // Para Address, DetailDTO y SimpleDTO son prácticamente iguales
        return AddressDetailDTO.builder()
                .id(entity.getId())
                .street(entity.getStreet())
                .number(entity.getNumber())
                .city(entity.getCity())
                .province(entity.getProvince())
                .build();
    }

    // =====================================================================
    // MAPEO DE CREACIÓN Y ACTUALIZACIÓN
    // =====================================================================

    @Override
    public Address toEntity(CreateAddressDTO createDTO) {
        if (isNull(createDTO)) {
            return null;
        }
        return Address.builder()
                .street(createDTO.getStreet())
                .number(createDTO.getNumber())
                .city(createDTO.getCity())
                .province(createDTO.getProvince())
                .country(createDTO.getCountry())
                .postalCode(createDTO.getPostalCode())
                .build();
    }

    /**
     * Crear entidad Address desde UpdateAddressDTO
     * CASO DE USO: Cuando usuario no tenía dirección y la crea via update
     * PRINCIPIO: Reutilizar lógica consistente del mapper
     */
    public Address toEntity(UpdateAddressDTO updateDTO) {
        if (isNull(updateDTO)) {
            return null;
        }

        return Address.builder()
                .street(updateDTO.getStreet())
                .number(updateDTO.getNumber())
                .city(updateDTO.getCity())
                .province(updateDTO.getProvince())
                .country(updateDTO.getCountry())
                .postalCode(updateDTO.getPostalCode())
                .build();
    }

    @Override
    public Address updateEntity(Address existingEntity, UpdateAddressDTO updateDTO) {
        if (isNull(existingEntity) || isNull(updateDTO)) {
            return existingEntity;
        }

        return Address.builder()
                .id(existingEntity.getId())
                .street(updateDTO.getStreet() != null ?
                       updateDTO.getStreet() : existingEntity.getStreet())
                .number(updateDTO.getNumber() != null ?
                       updateDTO.getNumber() : existingEntity.getNumber())
                .city(updateDTO.getCity() != null ?
                     updateDTO.getCity() : existingEntity.getCity())
                .province(updateDTO.getProvince() != null ?
                         updateDTO.getProvince() : existingEntity.getProvince())
                // ✅ CAMPOS OBLIGATORIOS que faltaban:
                .country(updateDTO.getCountry() != null ?
                        updateDTO.getCountry() : existingEntity.getCountry())
                .postalCode(updateDTO.getPostalCode() != null ?
                           updateDTO.getPostalCode() : existingEntity.getPostalCode())
                .build();
    }
    // =============================================================
    // MÉTODOS DE UTILIDAD
    // =============================================================
    /**
     * Verifica si un objeto es null
     */
    @Override
    public boolean isNull(Object obj) {
        return obj == null;
    }
}
