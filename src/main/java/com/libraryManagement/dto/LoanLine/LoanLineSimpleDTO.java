package com.libraryManagement.dto.LoanLine;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LoanLineSimpleDTO {
    private Long id;
    private Long copyId;
    private Long loanId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate estimatedReturnDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private  LocalDate actualReturnDate;

    private Boolean isActive;
    private String copyTitle;

}
