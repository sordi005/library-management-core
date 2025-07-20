package com.libraryManagement.mapper;

import com.libraryManagement.model.Genre;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(
    componentModel = "default",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface GenreMapper {

    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    @Mapping(target = "publicationCount", expression = "java(genre.getPublications().size())")
    GenreDTO toDTO(Genre genre);

    @Mapping(target = "publicationCount", ignore = true)
    GenreDTO toBasicDTO(Genre genre);

    //Convierte una lista de entidades a una lista de DTOs
    List<GenreDTO> toDTOs(List<Genre> genres);

    // Convierte una lista de entidades a una lista de DTOs básicos (sin conteo de publications)
    List<GenreDTO> toBasicDTOs(List<Genre> genres);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publications", ignore = true)
    @Mapping(target = "publicationCount", ignore = true)
    Genre toEntity(GenreDTO genreDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publications", ignore = true)
    @Mapping(target = "publicationCount", ignore = true)
    void updateEntityFromDTO(GenreDTO genreDTO, @MappingTarget Genre genre);

}