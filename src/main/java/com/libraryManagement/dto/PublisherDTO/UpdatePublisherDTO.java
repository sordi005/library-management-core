package com.libraryManagement.dto.PublisherDTO;

import com.libraryManagement.dto.AddressDTO.UpdateAddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdatePublisherDTO {
    @NotBlank(message = "Publisher name is required")
    @Size(max = 50, message = "Publisher name cannot exceed 50 characters")
    private String name;

    @Size(max = 100, message = "Website cannot exceed 100 characters")
    @Pattern(regexp = "^(https?://)?(www\\.)?[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}(/.*)?$",
            message = "Invalid website format")
    private String webSite;

    @Pattern(regexp = "^[+]?[0-9\\s\\-\\(\\)]{8,15}$",
            message = "Phone number must be between 8-15 characters and contain only numbers, spaces, hyphens, and parentheses")
    private String phone;

    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Email(message = "Invalid email format")
    private String email;

    @Valid
    private UpdateAddressDTO address;
}
