package com.libraryManagement.dto.CopyDTO;

import com.libraryManagement.dto.PublicationDTO.PublicationSimpleDTO;
import com.libraryManagement.model.enums.CopyStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CopyDetailDTO {
    private Long id;
    private Integer copyNumber;
    private CopyStatus status;
    private PublicationSimpleDTO publication;
    private Integer activeLoanCount;
}