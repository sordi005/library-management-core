package com.libraryManagement.mapper;

import com.libraryManagement.dto.AuthorDTO;
import com.libraryManagement.model.Author;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(
    componentModel = "default",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    /**
     * DTO MAPPERS
     */
    // Mapeo completo con conteo de publicaciones
    @Mapping(target = "publicationCount", expression = "java(author.getPublications().size())")
    AuthorDTO toDTO(Author author);

    // Mapeo básico sin conteo de publicaciones
    @Mapping(target = "publicationCount", ignore = true)
    AuthorDTO toBasicDTO(Author author);

    List<AuthorDTO> toDTOs(List<Author> authors);

    // Mapeo básico para listas sin conteo de publicaciones
    List<AuthorDTO> toBasicDTOs(List<Author> authors);

    /**
     * ENTITY MAPPERS
     */
    // Mapeo completo desde DTO a entidad
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicationCount", ignore = true)
    @Mapping(target = "publications", ignore = true)
    Author toEntity(AuthorDTO authorDTO);

    // Mapeo básico desde DTO a entidad sin publicaciones
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicationCount", ignore = true)
    @Mapping(target = "publications", ignore = true)
    void updateEntityFromDTO(AuthorDTO authorDTO, @MappingTarget Author author);



}