package com.libraryManagement.dto.AddressDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDetailDTO {
    private Long id;
    private String street;
    private String number;
    private String city;
    private String province;
    private String country;
    private String postalCode;
}
