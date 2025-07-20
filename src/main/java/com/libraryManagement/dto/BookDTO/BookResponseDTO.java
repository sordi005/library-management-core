package com.libraryManagement.dto.BookDTO;

import com.libraryManagement.dto.PublicationDTO.BasePublicationResponseDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class BookResponseDTO extends BasePublicationResponseDTO {
    private String isbn;
    private String seriesName; // Name of the series or saga the book belongs to
}
