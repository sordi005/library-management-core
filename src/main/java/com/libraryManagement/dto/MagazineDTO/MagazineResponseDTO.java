package com.libraryManagement.dto.MagazineDTO;

import com.libraryManagement.dto.PublicationDTO.BasePublicationResponseDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MagazineResponseDTO extends BasePublicationResponseDTO {
    private String issn;
    private String volume;
}
