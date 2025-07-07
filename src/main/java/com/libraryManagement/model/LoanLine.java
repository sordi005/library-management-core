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

    @EqualsAndHashCode.Include
    @ManyToOne(optional = false ,fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @EqualsAndHashCode.Include
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "copy_id")
    private Copy copy;

    @Column(name = "estimated_return_date",nullable = false)
    private LocalDate estimatedReturnDate;

    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;
}
