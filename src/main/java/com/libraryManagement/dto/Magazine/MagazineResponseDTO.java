package com.libraryManagement.dto.Magazine;

import com.libraryManagement.dto.Publication.BasePublicationResponseDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MagazineResponseDTO extends BasePublicationResponseDTO {
    private String issn;
    private String volume;
}
