package com.libraryManagement.dto.PublicationDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.libraryManagement.dto.AuthorDTO.AuthorSimpleDTO;
import com.libraryManagement.dto.BookDTO.BookDetailDTO;
import com.libraryManagement.dto.MagazineDTO.MagazineDetailDTO;
import com.libraryManagement.dto.PublisherDTO.PublisherSimpleDTO;
import com.libraryManagement.dto.genreDTO.GenreSimpleDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Data
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BookDetailDTO.class, name = "BOOK"),
        @JsonSubTypes.Type(value = MagazineDetailDTO.class, name = "MAGAZINE")
})
public abstract  class PublicationDetailDTO {
        private Long id;
        private String title;
        private String subTitle;
        @JsonFormat(pattern = "yyyy-MM-dd") // ✅ Formato correcto
        private LocalDate publicationDate;
        private String language;
        private String edition;
        private Integer pageCount;
        private String summary;

        //relaciones
        private GenreSimpleDTO genre; // Genre of the publication
        private PublisherSimpleDTO publisher; // Publisher of the publication

        //Authors
        private Set<AuthorSimpleDTO> authors;

        //Copy
        private Integer totalCopies; // Total number of copies available
        private Integer availableCopies; // Number of copies currently available for borrowing
        private Integer loanedCopies; // Number of copies currently on loan
        private Integer reservedCopies; // Number of copies currently reserved
}
