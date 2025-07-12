package com.libraryManagement.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "magazines")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SuperBuilder
public class Magazine extends Publication {

    @EqualsAndHashCode.Include
    @Column(name = "issn", nullable = false, length = 20, unique = true)
    private String issn;

    @Column(name = "volume", length = 10)
    private String volume;

}
