package com.libraryManagement.dto.AuthorDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorSimpleDTO {
    private Long id;
    private String name;
}
