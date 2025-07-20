package com.libraryManagement.dto.CopyDTO;

import com.libraryManagement.model.enums.CopyStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CopySimpleDTO {
    private Long id;
    private Integer copyNumber;
    private CopyStatus status;
    private String publicationTitle;
    private String publicationType;

}