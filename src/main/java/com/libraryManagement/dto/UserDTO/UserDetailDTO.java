package com.libraryManagement.dto.UserDTO;

import com.libraryManagement.dto.AddressDTO.AddressSimpleDTO;
import com.libraryManagement.dto.LoanDTO.LoanSimpleDTO;
import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class UserDetailDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;
    private String phoneNumber;
    private AddressSimpleDTO address;
    private Set<LoanSimpleDTO>loans;

    private Integer age;
    private String cityName;
    private String fullName;

    private Integer activeLoanCount;
    private boolean loanActive;
}
