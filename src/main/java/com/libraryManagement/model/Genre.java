package com.libraryManagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Table(name = "genre")
@Entity

@Getter
@Setter(AccessLevel.PROTECTED)
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true,callSuper = false)
@NoArgsConstructor
@SuperBuilder
public class Genre extends BaseEntity {
    @EqualsAndHashCode.Include
    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 255)
    private String description;
}
