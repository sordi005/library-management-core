package com.libraryManagement.dto.UserDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.libraryManagement.dto.AddressDTO.UpdateAddressDTO;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateUserDTO {

    // ✅ CAMPOS OPCIONALES - Para actualizaciones parciales
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName; // Removido @NotBlank

    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName; // Removido @NotBlank

    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth; // Removido @NotNull

    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Email(message = "Email should be valid")
    private String email; // Removido @NotBlank

    @Pattern(regexp = "^[+]?[0-9\\s\\-\\(\\)]{8,15}$",
            message = "Phone number must be between 8-15 characters and contain only numbers, spaces, hyphens, and parentheses")
    private String phoneNumber;

    private UpdateAddressDTO address;

}
