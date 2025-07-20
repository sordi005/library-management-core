package com.libraryManagement.dto.CopyDTO;

import com.libraryManagement.model.enums.CopyStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCopyDTO {
    @NotNull(message = "Copy number is required")
    @Positive(message = "Copy number must be positive")
    private Integer copyNumber;

    @NotNull(message = "Status is required")
    private CopyStatus status;
}
