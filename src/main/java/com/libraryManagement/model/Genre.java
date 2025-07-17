package com.libraryManagement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Table(name = "genre")
@Entity

@Getter
@Setter(AccessLevel.PROTECTED)
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true,callSuper = false)
@NoArgsConstructor
@SuperBuilder
public class Genre extends Base {
    @EqualsAndHashCode.Include
    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List <Publication> publications = new ArrayList<>();
    /**
     * Agrega una publicación a este género.
     * Sincroniza ambos lados de la relación.
     */
    public void addPublication(Publication publication) {
        if(publication == null) return;
        if (publications.contains(publication) && publication.getGenre() == this) return;

        if (!publications.contains(publication)) {
            publications.add(publication);
        }
        if (publication.getGenre() != this) {
        publication.setGenre(this);
        }
    }
    /**
     * Remueve una publicación de este género.
     * Sincroniza ambos lados de la relación.
     */
    public void removePublication(Publication publication) {
        if (publication == null ) return;
        if (!publications.contains(publication)) return;
        publications.remove(publication);
        if(publication.getGenre() == this) {
            publication.setGenre(null); // Desvincular la publicación del género
        }
    }

}
