package com.libraryManagement.dto.LoanLine;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.libraryManagement.dto.CopyDTO.CopySimpleDTO;
import com.libraryManagement.dto.LoanDTO.LoanSimpleDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanLineDetailDTO {
    private Long id;
    private CopySimpleDTO copyId;
    private LoanSimpleDTO loanId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String estimatedReturnDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String actualReturnDate;

    private Boolean isActive;
    private Boolean isOverdue;
    private Integer daysUntilDue;

    private String copyTitle;

}
