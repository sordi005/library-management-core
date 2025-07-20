package com.libraryManagement.dto.genreDTO;

import com.libraryManagement.dto.PublicationDTO.PublicationSimpleDTO;
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
