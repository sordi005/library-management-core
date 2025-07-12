package com.libraryManagement.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users",
        indexes = {
            @Index(name = "idx_user_email", columnList = "email"),
            @Index(name = "idx_user_dni", columnList = "dni")
        })
@Getter
@Setter()
@NoArgsConstructor
@AllArgsConstructor // Lombok annotations for boilerplate code
@ToString(callSuper = true) // Include fields from BaseEntity in toString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false) // Exclude id from equals/hashCode
@SuperBuilder
 /**
 * Clase que representa a un usuario del sistema.
 * <p>Extiende de BaseEntity para heredar campos comunes como id, createdAt y updatedAt.</p>
 * <p>Incluye validaciones para los campos de nombre, apellido, fecha de nacimiento, DNI, email y teléfono.</p>
 * <p>La relación con préstamos es bidireccional y se maneja mediante métodos para añadir y eliminar préstamos.</p>
  */
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
    private String phone;

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
