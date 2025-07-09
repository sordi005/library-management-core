package com.libraryManagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "publications", indexes = {
    @Index(name = "idx_publication_title", columnList = "title"),
    @Index(name = "idx_publication_language", columnList = "language"),
    @Index(name = "idx_publication_date", columnList = "publication_date")
})
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Publication extends BaseEntity {

    @EqualsAndHashCode.Include
    @NotBlank(message = "El título no puede estar vacío")
    @Column(nullable = false,length = 100)
    private String title;

    @Column(name = "sub_title",length = 100)
    private String subTitle;

    @Column(name = "publication_date")
    @PastOrPresent(message = "La fecha debe ser pasada o actual")
    private LocalDate publicationDate; // Fecha de publicación de la obra, no puede ser futura

    @Column(length = 50)
    private String language; // Idioma de la publicación (Ej: "ES", "EN", "FR")

    @Column(length = 100)
    private String edition; // Edición o versión específica del material.
    @Column(name = "page_count")
    private Integer pageCount; // Cantidad total de páginas del documento

    @Lob
    private String summary; // Breve descripción o sinopsis del contenido de la publicación

    //Relacion con Genre
    @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "genere_id", nullable = true)
    private Genre genre;

    //Relacion con Publisher
    @Setter(AccessLevel.NONE)
    @ManyToOne(optional = true, fetch =  FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = true)
    private Publisher publisher;

    //relacion con Copy
    @Builder.Default
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publication", cascade = CascadeType.ALL)
    private Set<Copy> copies = new HashSet<>();

    // Relación con Author
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "publication_author",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @Builder.Default
    @ToString.Exclude
    private Set<Author> authors = new HashSet<>();

    /**
     * Establece el editor para esta publicación.
     * Mantiene la relación bidireccional y evita duplicados en la lista de publicaciones del editor.
     */
    public void setPublisher(Publisher publisher) {
        if (publisher == null || this.publisher == publisher) return;
        this.publisher = publisher;
        if (!publisher.getPublications().contains(this)) {
            publisher.addPublication(this);
        }
    }
    /**
     * Agrega una Copia de el libro.
     * Mantiene la relación bidireccional y evita duplicados.
     */
    public void addCopy(Copy copy) {
        if (copy == null || copies.contains(copy))return;
        copies.add(copy);
        copy.setPublication(this);
    }
    /**
     * Agrega un author a la lista de autores.
     * Mantiene la relación bidireccional y evita duplicados.
     */
    public void addAuthor(Author author) {
        if (author == null || authors.contains(author)) return;
        authors.add(author);
        author.addPublication(this); // sincroniza el otro lado
    }
}

