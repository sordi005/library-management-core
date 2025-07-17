package com.libraryManagement.dto.Author;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UpdateAuthorDTO {
    @NotBlank(message = "Author name is required")
    @Size(max = 50, message = "Author name cannot exceed 50 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s.-]+$", message = "Invalid author name format")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Size(max = 50, message = "County cannot exceed 50 characters")
    private String county;
}
