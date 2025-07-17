package com.libraryManagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class PublicationResponseDTO {

    private Long id;

    private String title;

    private String subTitle;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    private String language;

    private String edition;

    private Integer pageCount;

    private String summary;

    // Solo nombres para evitar referencias circulares
    private String genreName;

    private String publisherName;

    // Concatenación de autores
    private String authors;

    // Estadísticas de copias
    private Integer totalCopies;

    private Integer availableCopies;

}