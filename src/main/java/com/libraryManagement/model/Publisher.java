package com.libraryManagement.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "publishers")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper=true)
@SuperBuilder
public class Publisher extends BaseEntity {

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(name = "web_site")
    private String webSite;

    @Column
    @Pattern(regexp = "^\\+?[0-9]{8,15}$")
    private String phone;

    @Column
    @Email
    private String email;

    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_id")
    private Address address;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "publisher")
    private List<Publication> publications = new ArrayList<>();

    public void addPublication(Publication publication) {
        if (publication == null || publications.contains(publication)) return;
        publications.add(publication);
        publication.setPublisher(this);

    }
}
