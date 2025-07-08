package com.libraryManagement.model;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

@Entity
@Table(name = "loan_lines")

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = false, callSuper = false)
@SuperBuilder
public class LoanLine extends BaseEntity {

    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include
    @ManyToOne(optional = false ,fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "copy_id",nullable = false)
    private Copy copy;

    @Column(name = "estimated_return_date",nullable = false)
    private LocalDate estimatedReturnDate;

    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;

    /**
     * Mantiene la relación bidireccional y evita duplicados en la lista de Loan.
     */
    public void setLoan(Loan loan) {
        if (loan == null || this.loan == loan) return;
        this.loan = loan;
        if (!loan.getLoanLines().contains(this)) {
            loan.addLoanLine(this);
        }
    }
    public void setCopy(Copy copy) {
        if (copy == null || this.copy == copy) return;
        this.copy = copy;
        if (!copy.getLoanLines().contains(this)) {
            copy.addLoanLine(this);
        }
    }
}
