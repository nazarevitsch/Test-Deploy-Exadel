package com.exadel.discount.platform.model.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class SubCategoryDto {
    @NotBlank
    @Size(min = 3, max = 20)
    private String name;
//    @ToString.Exclude
//    private List<DiscountDto> discounts;
}
