package com.libraryManagement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "loans")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SuperBuilder
public class Loan extends BaseEntity {

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "loan",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<LoanLine> loanLines = new HashSet<>();

    /**
     * Establece el usuario para este préstamo.
     * Mantiene la relación bidireccional y evita duplicados en la lista de préstamos del usuario.
     */
    public void setUser(User user) {
        if(user == null || this.user == user) return;
        this.user = user;
        if(!user.getLoans().contains(this)) {
            user.addLoan(this);
        };

    }
    /**
     * Agrega una línea de préstamo a este préstamo.
     * Mantiene la relación bidireccional y evita duplicados.
     */
    public void addLoanLine(LoanLine loanLine) {
        if (this.loanLines == null || loanLines.contains(loanLine)) return;
        loanLines.add(loanLine);
        loanLine.setLoan(this);
    }
}
