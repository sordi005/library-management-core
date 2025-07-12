package com.libraryManagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "books")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SuperBuilder
public class Book extends Publication {
    /**
     * Exiende de Publication y representa un libro específico.
     * Atributo heredado Obligatorio:
     * - title (Título del libro)
     */
    @EqualsAndHashCode.Include
    @Column(nullable = false,unique = true)
    private String isbn; // ISBN del libro, único para cada edición (Obligatorio)

    @Column(name = "series_name", length = 100)
    private String seriesName; // Nombre de la colección o saga a la que pertenece el libro (Ej: “Harry Potter”)

    @Column(name = "series_order")
    private Integer seriesOrder; // Número dentro de la serie o colección (Ej: “1”, “2”)

    @Column(name = "original_language", length = 50)
    private String originalLanguage; // Idioma original si fue traducido (Ej: “Inglés”, “Francés”)

    @Lob
    @Column(name = "table_of_contents")
    private String tableOfContents; // Contenido estructurado del libro: capítulos, secciones, etc.
}
