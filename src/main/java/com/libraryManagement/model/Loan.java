package com.libraryManagement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.h2.command.dml.Set;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SuperBuilder
public class Loan extends BaseEntity {

    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @EqualsAndHashCode.Include
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @EqualsAndHashCode.Include
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "loan",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<LoanLine> loanLines = new ArrayList<>();
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
