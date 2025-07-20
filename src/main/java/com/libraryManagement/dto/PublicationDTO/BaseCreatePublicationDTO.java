package com.libraryManagement.dto.PublicationDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.libraryManagement.dto.BookDTO.CreateBookDTO;
import com.libraryManagement.dto.MagazineDTO.CreateMagazineDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Data
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateBookDTO.class, name = "BOOK"),
        @JsonSubTypes.Type(value = CreateMagazineDTO.class, name = "MAGAZINE")
})
public abstract class BaseCreatePublicationDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;

    @Size(max = 50, message = "Subtitle cannot exceed 50 characters")
    private String subTitle;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    @Size(max = 15, message = "Language cannot exceed 15 characters")
    private String language;

    private Long genreId;

    private Long publisherId;

    @Size(max = 100, message = "Edition cannot exceed 100 characters")
    private String edition;

    private Integer pageCount;

    @Size(max = 1000, message = "Summary cannot exceed 1000 characters")
    private String summary;

    private Set<Long> authorIds;
}
