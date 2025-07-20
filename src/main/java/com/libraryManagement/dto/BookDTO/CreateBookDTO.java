package com.libraryManagement.dto.BookDTO;

import com.libraryManagement.dto.PublicationDTO.BaseCreatePublicationDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CreateBookDTO extends BaseCreatePublicationDTO {

    @NotBlank(message = "ISBN is required")
    @Size(max = 13, message = "ISBN cannot exceed 13 characters")
    private String isbn;

    @Size(max = 100, message = "Series name cannot exceed 100 characters")
    private String seriesName;

    @Positive(message = "Series order must be positive")
    @Size(max = 3, message = "Series order cannot exceed 3 digits")
    private Integer seriesOrder;

    @Size(max = 20, message = "Original language cannot exceed 20 characters")
    private String originalLanguage;

    @Size(max = 2000, message = "Table of contents cannot exceed 2000 characters")
    private String tableOfContents;

}
