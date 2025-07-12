package com.libraryManagement.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "addresses")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class Address extends BaseEntity {

    @Column(name = "street", nullable = false, length = 30)
    private String street;

    @Column(name = "number", nullable = false, length = 10)
    private String number;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "province", length = 50)
    private String province;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

}
