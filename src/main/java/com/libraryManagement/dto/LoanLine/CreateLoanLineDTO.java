package com.libraryManagement.dto.LoanLine;

import jakarta.validation.constraints.NotNull;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;

import java.time.LocalDate;

public class CreateLoanLineDTO {
    @NotNull(message = "Copy ID cannot be null")
    private Long copyId;
    @NotNull(message = "Loan ID cannot be null")
    private Long loanId;
    @NotNull(message = "Estimated return date cannot be null")
    private LocalDate estimatedReturnDate;
    @NotNull(message = "Actual return date cannot be null")
    private LocalDate actualReturnDate;
}
