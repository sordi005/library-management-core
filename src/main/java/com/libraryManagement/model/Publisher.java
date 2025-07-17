package com.libraryManagement.model;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "publishers")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@ToString(callSuper=true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SuperBuilder
public class Publisher extends Base {

    @EqualsAndHashCode.Include
    @Column(nullable = false)
    private String name;

    @Column(name = "web_site")
    private String webSite;

    @Column(length = 16)
    private String phone;

    @Column(length = 100, unique = true)
    private String email;

    @ToString.Exclude
    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_id")
    private Address address;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "publisher",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Publication> publications = new HashSet<>();


    public void addPublication(Publication publication) {
        if (publication == null || publications.contains(publication)) return;
        publications.add(publication);
        publication.setPublisher(this);
    }

    public void removePublication(Publication publication) {
        if (publication == null || !publications.contains(publication)) return;
        publications.remove(publication);
        publication.setPublisher(null);
    }
}
