package com.exadel.discount.platform.model.dto;


import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class VendorDto {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
    @Size(min = 50, max = 200)
    private String description;
    @ToString.Exclude
    private List<DiscountDto> discountDtos;
    private boolean deleted;
}
