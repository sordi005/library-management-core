package com.libraryManagement.dto.Address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAddressDTO {
    @NotBlank(message = "Street cannot be blank")
    @Size(max = 30, message = "Street must be at most 30 characters long")
    private String street;
    @NotBlank(message = "Number cannot be blank")
    @Pattern(regexp = "^[0-9A-Za-z\\s\\-]{1,10}$", message = "Invalid number format")
    private String number;
    @NotBlank(message = "City cannot be blank")
    @Size(max = 50, message = "City must be at most 50 characters long")
    private String city;
    @NotBlank(message = "Province cannot be blank")
    @Size(max = 50, message = "Province must be at most 50 characters long")
    private String province;
    @NotBlank(message = "Country cannot be blank")
    @Size(max = 50, message = "Country must be at most 50 characters long")
    private String country;
    @NotBlank(message = "Postal code cannot be blank")
    @Pattern(regexp = "^[0-9]{5}$", message = "Postal code must be 5 digits")
    private String postalCode;
}
