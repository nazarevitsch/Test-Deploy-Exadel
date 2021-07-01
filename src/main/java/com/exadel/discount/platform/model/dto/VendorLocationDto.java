package com.exadel.discount.platform.model.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
public class VendorLocationDto {

    @NotBlank
    @Size(min = 3, max = 60)
    private String country;
    @Size(min = 2, max = 50)
    private String city;
    @Size(min = 2, max = 50)
    private String addressLine;
    @NotNull
    private UUID vendorId;
    @ToString.Exclude
    private List<DiscountDto> discounts;
    /*private int latitude;
    private int longitude;*/
}
