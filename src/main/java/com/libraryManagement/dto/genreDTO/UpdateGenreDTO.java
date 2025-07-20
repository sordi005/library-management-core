package com.libraryManagement.dto.genreDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateGenreDTO {
    @NotBlank(message = "Genre name is required")
    @Size(max = 50, message = "Genre name cannot exceed 50 characters")
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
}
