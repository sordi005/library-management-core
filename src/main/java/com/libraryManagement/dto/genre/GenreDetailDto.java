package com.libraryManagement.dto.genre;

import com.libraryManagement.dto.Publication.PublicationSimpleDTO;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class GenreDetailDto {
    private Long id;
    private String name;
    private String description;
    private Set<PublicationSimpleDTO> publications;
    private Integer publicationCount;
}
