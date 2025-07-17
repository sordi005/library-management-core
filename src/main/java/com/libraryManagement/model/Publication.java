package com.libraryManagement.model;

import jakarta.persistence.*;
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
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Publication extends Base {

    @EqualsAndHashCode.Include
    @Column(nullable = false,length = 50)
    private String title;

    @Column(name = "sub_title",length = 50)
    private String subTitle;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(length = 15)
    private String language;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "genre_id", nullable = true)
    private Genre genre;

    @ManyToOne(optional = true, fetch =  FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = true)
    private Publisher publisher;

    @Column(length = 100)
    private String edition; // Edición o versión específica del material, útil para libros, revistas y tesis

    @Column(name = "page_count")
    private Integer pageCount; // Cantidad total de páginas del documento

    @Lob
    @Column(name = "summary", columnDefinition = "TEXT", length = 1000)
    private String summary; // Breve descripción o sinopsis del contenido de la publicación

    @Builder.Default
    @ToString.Exclude
    @OneToMany(
            mappedBy = "publication", // Nombre del campo en la entidad Copy
            fetch= FetchType.LAZY, // Carga perezosa para evitar problemas de rendimiento
            cascade={CascadeType.ALL}, // Cascada para que las copias se manejen junto con la publicación
            orphanRemoval = true) // Eliminar copias huérfanas si se eliminan de la colección
    private Set<Copy> copies = new HashSet<>();

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
     * Establece el género de esta publicación.
     * Sincroniza ambos lados de la relación.
     */
    public void setGenre(Genre genre) {
        // Remover del género anterior
        if (this.genre != null) {
            this.genre.removePublication(this);
        }
        this.genre = genre;

        // Agregar al nuevo género
        if (genre != null) {
            genre.addPublication(this);
        }
    }
    /**
     * Agrega una copia a esta publicación.
     * Sincroniza ambos lados de la relación.
     */
    public void addCopy(Copy copy) {
        if (copy == null || copies.contains(copy))return;
        copies.add(copy);
        copy.setPublication(this);
    }
    /**
     * Agrega un autor a esta publicación.
     * Sincroniza ambos lados de la relación.
     */
    public void addAuthor(Author author) {
        if (author == null || authors.contains(author)) return;
        authors.add(author);
        author.addPublication(this); // sincroniza el otro lado
    }
    /**
     * Remueve un autor de esta publicación.
     * Sincroniza ambos lados de la relación.
     */
    public void removeAuthor(Author author) {
        if (author == null || !authors.contains(author)) return;
        authors.remove(author);
        if (author.getPublications().contains(this)) {
            author.removePublication(this);
        }
    }
}

