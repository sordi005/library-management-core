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
    private LocalDate publicationDate;

    @Column(length = 50)
    private String language;

    @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "genere_id", nullable = true)
    private Genre genre;

    @ManyToOne(optional = true, fetch =  FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = true)
    private Publisher publisher;

    @Column(length = 100)
    private String edition; // Edición o versión específica del material, útil para libros, revistas y tesis

    @Column(name = "page_count")
    private Integer pageCount; // Cantidad total de páginas del documento

    @Lob
    private String summary; // Breve descripción o sinopsis del contenido de la publicación

    /*@Builder.Default
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publication", cascade = CascadeType.ALL)
    private Set<Copy> copies = new HashSet<>();*/

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

    public void addCopy(Copy copy) {
        if (copy == null || copies.contains(copy))return;
        copies.add(copy);
        copy.setPublication(this);
    }

    public void addAuthor(Author author) {
        if (author == null || authors.contains(author)) return;
        authors.add(author);
        author.addPublication(this); // sincroniza el otro lado
    }
}

