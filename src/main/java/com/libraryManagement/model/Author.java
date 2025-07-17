package com.libraryManagement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "authors")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class Author extends Base {
    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 50)
    private String county;

    @ManyToMany(mappedBy = "authors")
    @Builder.Default
    @ToString.Exclude
    private Set<Publication> publications = new HashSet<>();

    public void addPublication(Publication publication) {
            if (publication == null ||publications.contains(publication)) return;
            publications.add(publication);
            if (!publication.getAuthors().contains(this)) {
                publication.addAuthor(this);
            }
    }
    public void removePublication(Publication publication) {
        if (publication == null ) return;
        if (!publications.contains(publication)) return;
        publications.remove(publication);
        if(publication.getAuthors().contains(this)) {
            publication.removeAuthor(this);
        }
    }

}
