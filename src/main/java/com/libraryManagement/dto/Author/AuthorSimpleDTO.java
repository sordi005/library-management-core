package com.libraryManagement.dto.Author;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AuthorSimpleDTO {
    private Long id;
    private String name;
}
