package com.libraryManagement.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class Address extends BaseEntity {

    @NotBlank
    @Size(max = 30)
    @Column(name = "street", nullable = false, length = 30)
    private String street;

    @NotBlank
    @Size(max = 10)
    @Column(name = "number", nullable = false, length = 10)
    private String number;

    @NotBlank
    @Size(max = 50)
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Size(max = 50)
    @Column(name = "province", length = 50)
    private String province;

    @NotBlank
    @Size(max = 50)
    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @NotBlank
    @Size(max = 10)
    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

}
