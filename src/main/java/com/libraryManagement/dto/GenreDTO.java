package com.libraryManagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreDTO {

    private Long id;

    @NotBlank(message = "Genre name is required")
    @Size(max = 50, message = "Genre name cannot exceed 50 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s-]+$", message = "Invalid genre name format")
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @Min(value = 0, message = "El número de publicaciones no puede ser negativo")
    @JsonProperty("publication_count")
    private int publicationCount; // Campo calculado para respuestas

}