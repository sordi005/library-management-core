package com.libraryManagement.dto.UserDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.libraryManagement.dto.AddressDTO.AddressSimpleDTO;
import com.libraryManagement.dto.LoanDTO.LoanSimpleDTO;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@Jacksonized  // 🔥 SOLUCIÓN: Permite que Jackson serialice el DTO a JSON
public class UserDetailDTO {
    private Long id;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String dni;
    private String email;
    private String phoneNumber;
    private AddressSimpleDTO address;
    private Set<LoanSimpleDTO>loans;

    private Integer age;
    private String cityName;
    private String fullName;

    private Integer activeLoanCount;
    private Boolean loanActive; //
}
