package com.libraryManagement.dto.LoanDTO;

import com.libraryManagement.dto.LoanLine.UpdateLoanLineDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public class UpdateLoanDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "Due Date date is required")
    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    private LocalDate returnAt;

    @NotEmpty(message = "At least one loan line is required")
    @Size(min = 1, max = 5, message = "Number of loan lines must be between 1 and 5")
    @Valid  // ✅ Validación en cascada
    private Set<UpdateLoanLineDTO> loanLines;

}
