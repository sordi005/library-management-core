package com.libraryManagement.dto.genre;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreSimpleDTO {
    private Long id;
    private String name;
}
