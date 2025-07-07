package com.libraryManagement.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SuperBuilder
public class User extends BaseEntity {

    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @EqualsAndHashCode.Include
    @NotBlank
    @Column(nullable = false,unique = true)
    private String dni;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$")
    private String phone;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Loan> loans = new ArrayList<>();


    public void addLoan(Loan loan) {
        if (loan == null || loans.contains(loan))return;
        loans.add(loan);
        loan.setUser(this);
    }
}
