package com.exadel.discount.platform.model.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class VendorLocationDto {

    @NotBlank
    @Size(min = 3, max = 60)
    private String country;
    @Size(min = 2, max = 50)
    private String city;
    @Size(min = 2, max = 50)
    private String addressLine;
    private VendorDto vendorDto;
    private boolean deleted;
    @ToString.Exclude
    private List<DiscountDto> discounts;
    /*private int latitude;
    private int longitude;*/
}
