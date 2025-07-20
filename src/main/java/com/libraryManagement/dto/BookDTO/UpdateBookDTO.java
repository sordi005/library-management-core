package com.libraryManagement.dto.BookDTO;

import com.libraryManagement.dto.PublicationDTO.BaseUpdatePublicationDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class UpdateBookDTO extends BaseUpdatePublicationDTO {
    @NotBlank(message = "ISBN is required")
    @Size(max = 13, message = "ISBN cannot exceed 13 characters")
    private String isbn;

    @Size(max = 100, message = "Series name cannot exceed 100 characters")
    private String seriesName;

    @Positive(message = "Series order must be positive")
    private Integer seriesOrder;

    @Size(max = 30, message = "Original language cannot exceed 30 characters")
    private String originalLanguage;

    @Size(max = 2000, message = "Table of contents cannot exceed 2000 characters")
    private String tableOfContents;
}
