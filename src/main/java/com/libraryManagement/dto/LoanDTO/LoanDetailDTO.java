package com.libraryManagement.dto.LoanDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.libraryManagement.dto.LoanLine.LoanLineSimpleDTO;
import com.libraryManagement.dto.UserDTO.UserSimpleDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class LoanDetailDTO {
    private Long id;

    private UserSimpleDTO user;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnAt;

    Set<LoanLineSimpleDTO> loanLines;

    //vereifica si el préstamo está activo o no
    private Boolean isActive;
    //verifica si el préstamo está vencido o no
    private Boolean isOverdue;
    //cantidad de líneas de préstamo asociadas a este préstamo
    private Integer LoanLineCount;
    // cantidad de días restantes hasta la fecha de vencimiento
    private Integer daysUntilDue;

}
