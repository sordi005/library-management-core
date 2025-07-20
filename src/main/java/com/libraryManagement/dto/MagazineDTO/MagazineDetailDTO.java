package com.libraryManagement.dto.MagazineDTO;

import com.libraryManagement.dto.PublicationDTO.PublicationDetailDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MagazineDetailDTO extends PublicationDetailDTO {
    private String issn;
    private String volume;
}
