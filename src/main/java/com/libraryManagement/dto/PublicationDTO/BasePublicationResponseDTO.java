package com.libraryManagement.dto.PublicationDTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.libraryManagement.dto.BookDTO.BookResponseDTO;
import com.libraryManagement.dto.MagazineDTO.MagazineResponseDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BookResponseDTO.class, name = "BOOK"),
        @JsonSubTypes.Type(value = MagazineResponseDTO.class, name = "MAGAZINE")
})
public abstract class BasePublicationResponseDTO {

    private Long id;
    private String title;
    private String subTitle;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    private String language;
    private String edition;
    private Integer pageCount;
    private String summary;

    private String genreName;
    private String publisherName;
    private String authors;

    private Integer totalCopies;
    private Integer availableCopies;

}