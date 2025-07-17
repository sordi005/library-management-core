package com.libraryManagement.dto.Publication;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.libraryManagement.dto.Author.AuthorSimpleDTO;
import com.libraryManagement.dto.Book.BookDetailDTO;
import com.libraryManagement.dto.Magazine.MagazineSimpleDTO;
import com.libraryManagement.dto.Publisher.PublisherSimpleDTO;
import com.libraryManagement.dto.genre.GenreSimpleDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BookDetailDTO.class, name = "BOOK"),
        @JsonSubTypes.Type(value = MagazineSimpleDTO.class, name = "MAGAZINE")
})
public class PublicationDetailDTO {
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
        private GenreSimpleDTO Genre; // Genre of the publication
        private PublisherSimpleDTO Publisher; // Publisher of the publication

        //Authors
        private Set<AuthorSimpleDTO> authors;

        //Copy
        private Integer totalCopies; // Total number of copies available
        private Integer availableCopies; // Number of copies currently available for borrowing
        private Integer loanCopies; // Number of copies currently on loan
        private Integer reservedCopies; // Number of copies currently reserved
}
