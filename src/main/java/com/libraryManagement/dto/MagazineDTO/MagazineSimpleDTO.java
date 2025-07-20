package com.libraryManagement.dto.MagazineDTO;

import com.libraryManagement.dto.PublicationDTO.PublicationSimpleDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MagazineSimpleDTO extends PublicationSimpleDTO {
    private String issn; // International Standard Serial Number for the magazine
    private String volume; // Volume of the magazine
}
