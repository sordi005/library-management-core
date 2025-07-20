package com.libraryManagement.dto.LoanDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LoanSimpleDTO {
    private Long id;
    private String userFullName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private Boolean isActive;
    private Boolean isOverdue;
    private Integer numberOfLoanLines;
}
