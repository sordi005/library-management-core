package com.libraryManagement.dto.UserDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserSimpleDTO {
    // ✅ Campos básicos que ya tenías
    private Long id;
    private String fullName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String dni;
    private String email;
    private String phoneNumber;

    // ✅ Campos calculados
    private Integer age;
    private String cityName;
    private Boolean loanActive;
}

