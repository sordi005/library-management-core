package com.libraryManagement.dto.Publisher;

import com.libraryManagement.dto.Address.AddressDetailDTO;
import com.libraryManagement.dto.Address.AddressSimpleDTO;
import com.libraryManagement.model.Address;

import java.util.List;

public class PublisherDetailDTO {
    private String name;
    private String webSite;
    private String phone;
    private String email;
    private AddressDetailDTO address;
    private List<PublisherSimpleDTO> publications;
    private Integer publicationCount;
}
