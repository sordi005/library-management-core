package com.libraryManagement.dto.BookDTO;

import com.libraryManagement.dto.PublicationDTO.PublicationDetailDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SuperBuilder
public class BookDetailDTO extends PublicationDetailDTO {

    private String isbn; // ISBN del libro, único para cada edición (Obligatorio)
    private String seriesName; // Nombre de la colección o saga a la que pertenece el libro (Ej: “Harry Potter”)
    private Integer seriesOrder; // Número dentro de la serie o colección (Ej: “1”, “2”)
    private String originalLanguage; // Idioma original si fue traducido (Ej: “Inglés”, “Francés”)
    private String tableOfContents; // Contenido estructurado del libro: capítulos, secciones, etc.

}
