package com.libraryManagement.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDTO {

    private Long id;

    @NotBlank(message = "Author name is required")
    @Size(max = 50, message = "Author name cannot exceed 50 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s.-]+$", message = "Invalid author name format")
    private String name;

    @NotBlank(message = "County is required")
    @Size(max = 50, message = "County cannot exceed 50 characters")
    private String county;

    private Integer publicationCount; // Campo calculado para respuestas
}