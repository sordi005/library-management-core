package com.libraryManagement.dto.PublicationDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.libraryManagement.dto.BookDTO.BookSimpleDTO;
import com.libraryManagement.dto.MagazineDTO.MagazineSimpleDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BookSimpleDTO.class, name = "BOOK"),
        @JsonSubTypes.Type(value = MagazineSimpleDTO.class, name = "MAGAZINE")
})
public abstract class PublicationSimpleDTO {

    private Long id; // Identificador único de la publicación
    private String title; // Título principal de la publicación

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate; // Fecha de publicación en formato ISO (YYYY-MM-DD)

    private String language; // Idioma de la publicación (ej: "Español", "Inglés")
    private String genreName; // Nombre del género al que pertenece (ej: "Ficción", "Ciencia")
    private String publisherName; // Nombre de la editorial que publicó el material
    private String authors; // Nombres de todos los autores concatenados en una sola cadena

    private Integer totalCopies; // Cantidad total de copias físicas disponibles en la biblioteca
    private Integer availableCopies; // Cantidad de copias disponibles para préstamo (no prestadas)
}
