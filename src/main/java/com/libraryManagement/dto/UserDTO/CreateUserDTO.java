package com.libraryManagement.dto.UserDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.libraryManagement.dto.AddressDTO.CreateAddressDTO;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateUserDTO {

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(message = "DNI is required")
    @Size(min = 8, max = 8, message = "DNI must be exactly 8 characters")
    private String dni;

    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^[+]?[0-9\\s\\-\\(\\)]{8,15}$",
            message = "Phone number must be between 8-15 characters and contain only numbers, spaces, hyphens, and parentheses")
    private String phoneNumber;

    private CreateAddressDTO address;

}
