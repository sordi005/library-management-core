package com.libraryManagement.dto.Copy;

import com.libraryManagement.dto.Publication.PublicationSimpleDTO;
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