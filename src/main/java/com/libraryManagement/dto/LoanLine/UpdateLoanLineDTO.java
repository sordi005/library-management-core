package com.libraryManagement.dto.LoanLine;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateLoanLineDTO {
    @NotNull(message = "Copy ID cannot be null")
    private Long copyId;
    @NotNull(message = "Loan ID cannot be null")
    private Long loanId;
    @NotNull(message = "Estimated return date cannot be null")
    private LocalDate estimatedReturnDate;
    @NotNull(message = "Actual return date cannot be null")
    private LocalDate actualReturnDate;
}
