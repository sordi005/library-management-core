package com.libraryManagement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(name = "users",
        indexes = {
            @Index(name = "idx_user_email", columnList = "email"),
            @Index(name = "idx_user_dni", columnList = "dni")
        })
@Getter
@Setter // ✅ CAMBIAR A PACKAGE-PRIVATE (sin AccessLevel)
@NoArgsConstructor
@AllArgsConstructor // Lombok annotations for boilerplate code
@ToString(callSuper = true) // Include fields from BaseEntity in toString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false) // Exclude id from equals/hashCode
@SuperBuilder
public class User extends Base {

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

    // =====================================================================
    // MÉTODOS DE CONSULTA DE ESTADO - Solo lectura del estado actual
    // =====================================================================

    /**
     * Verifica si el usuario tiene préstamos activos
     * @return true si tiene préstamos sin devolver
     */
    public boolean hasActiveLoans() {
        return loans.stream().anyMatch(loan -> loan.getReturnedAt() == null);
    }

    /**
     * Cuenta los préstamos activos del usuario
     * @return número de préstamos sin devolver
     */
    public int getActiveLoanCount() {
        return (int) loans.stream()
            .filter(loan -> loan.getReturnedAt() == null)
            .count();
    }

    /**
     * Obtiene el nombre completo del usuario
     * @return firstName + " " + lastName (manejo seguro de nulls)
     */
    public String getFullName() {
        if (firstName == null && lastName == null) return "Sin nombre";
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }

    /**
     * Obtiene todos los préstamos activos del usuario
     * @return stream de préstamos sin fecha de retorno
     */
    public Stream<Loan> getActiveLoans() {
        return loans.stream().filter(loan -> loan.getReturnedAt() == null);
    }
}
