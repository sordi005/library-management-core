package com.libraryManagement.dto.Magazine;

import com.libraryManagement.dto.Publication.PublicationDetailDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MagazineDetailDTO extends PublicationDetailDTO {
    private String issn;
    private String volume;
}
