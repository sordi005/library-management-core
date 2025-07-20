package com.libraryManagement.mapper;

import com.libraryManagement.model.Address;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(
    componentModel = "default",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    AddressDTO toDTO(Address address);

    @Mapping(target = "id", ignore = true)
    Address toEntity(AddressDTO addressDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(AddressDTO addressDTO, @MappingTarget Address address);

    List<AddressDTO> toDTOs(List<Address> addresses);
}