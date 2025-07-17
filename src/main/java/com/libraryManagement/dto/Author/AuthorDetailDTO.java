package com.libraryManagement.dto.Author;

import com.libraryManagement.dto.Publication.PublicationSimpleDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class AuthorDetailDTO {
    private Long id;
    private String name;
    private String county;
    private LocalDate dateOfBirth; // Using String to handle date formatting in the DTO
    private Set<PublicationSimpleDTO> publications;
    private Integer publicationCount;
}
