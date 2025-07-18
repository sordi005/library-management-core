package com.libraryManagement.dto.Address;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressSimpleDTO {
    private Long id;
    private String street;
    private String number;
    private String city;
    private String province;
}
