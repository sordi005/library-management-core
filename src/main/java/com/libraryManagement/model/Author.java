package com.libraryManagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class Author extends BaseEntity {
    @NotBlank
    @Column(nullable = false, length = 50)
    private String name;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String county;

    @ManyToMany(mappedBy = "authors")
    @Builder.Default
    @ToString.Exclude
    private List<Publication> publications = new ArrayList<>();

    public void addPublication(Publication publication) {
            if (publication == null ||publications.contains(publication)) return;
            publications.add(publication);
            if (!publication.getAuthors().contains(this)) {
                publication.addAuthor(this);
            }
    }

}
