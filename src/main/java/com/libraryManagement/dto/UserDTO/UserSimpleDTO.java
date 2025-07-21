package com.libraryManagement.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSimpleDTO {
    // ✅ Campos básicos que ya tenías
    private Long id;
    private String fullName;
    private String dni;
    private String email;
    private String phoneNumber;

    // ✅ Campos calculados
    private Integer age;
    private String cityName;
    private Boolean loanActive;
}

