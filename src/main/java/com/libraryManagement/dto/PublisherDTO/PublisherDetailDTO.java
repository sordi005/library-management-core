package com.libraryManagement.dto.PublisherDTO;

import com.libraryManagement.dto.AddressDTO.AddressDetailDTO;

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
