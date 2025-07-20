package com.libraryManagement.dto.BookDTO;

import com.libraryManagement.dto.PublicationDTO.PublicationSimpleDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class BookSimpleDTO extends PublicationSimpleDTO {
    private String isbn;
    private String seriesName; // Name of the series or saga the book belongs to
}
