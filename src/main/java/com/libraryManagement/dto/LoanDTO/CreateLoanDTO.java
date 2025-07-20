package com.libraryManagement.dto.LoanDTO;

import com.libraryManagement.dto.LoanLine.CreateLoanLineDTO;
import com.libraryManagement.dto.UserDTO.UserSimpleDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class CreateLoanDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "Due Date date is required")
    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    @NotEmpty(message = "At least one loan line is required")
    @Size(min = 1, max = 5, message = "Number of loan lines must be between 1 and 5")
    @Valid
    private Set<CreateLoanLineDTO> loanLineIds;
}
