package com.libraryManagement.dto.Magazine;

import com.libraryManagement.dto.Publication.BaseUpdatePublicationDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class UpdateMagazineDTO extends BaseUpdatePublicationDTO {

    @NotBlank(message = "ISSN is required")
    @Size(max = 8, message = "ISSN cannot exceed 8 characters")
    private String issn;

    @Size(max = 50, message = "Volume cannot exceed 10 characters")
    private String volume;
}
