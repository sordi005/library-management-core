package com.libraryManagement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users",
        indexes = {
            @Index(name = "idx_user_email", columnList = "email"),
            @Index(name = "idx_user_dni", columnList = "dni")
        })
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor // Lombok annotations for boilerplate code
@ToString(callSuper = true) // Include fields from BaseEntity in toString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false) // Exclude id from equals/hashCode
@SuperBuilder
public class User extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    /**
     * Número de documento único (formato: 8 dígitos)
     * No cambia después de la creación
     */
    @EqualsAndHashCode.Include
    @Column(updatable = false, nullable = false,unique = true)
    private String dni;

    @Column(unique = true,length = 100)
    private String email;
    //
    @Column(length = 16)
    private String phoneNumber;

    /**
     * Relación con Address como objeto separado
     * <p>La relación es opcional, puede ser null.</p>
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Loan> loans = new HashSet<>();
    /**
     * Añade un préstamo al usuario.
     * <p>Mantiene la consistencia bidireccional de la relación.</p>
     *
     * @param loan Préstamo a añadir (no nulo)
     */
    public void addLoan(Loan loan) {
        if (loan == null || loans.contains(loan))return;
        loans.add(loan);
        loan.setUser(this);
    }
    /**
     * Elimina un préstamo del usuario.
     * <p>Mantiene la consistencia bidireccional de la relación.</p>
     *
     * @param loan Préstamo a eliminar (no nulo)
     */
    public void removeLoan(Loan loan) {
        if (loan == null || !loans.contains(loan)) return;
        loans.remove(loan);
        loan.setUser(null);
    }

}
