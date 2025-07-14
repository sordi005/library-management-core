package com.libraryManagement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class UserDTO {

    private Long id; // Solo para updates

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "Debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "No puede contener números ni caracteres especiales")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "No puede contener números ni caracteres especiales")
    @JsonProperty("last_name")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @NotBlank(message = "DNI is required")
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 dígitos")
    private String dni;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @JsonProperty("phone_number")
    private String phoneNumber;

    // Relación con Address como objeto separado
    @Valid  // Valida el objeto Address internamente
    private AddressDTO addressDTO;

    private Integer loanCount;

}

