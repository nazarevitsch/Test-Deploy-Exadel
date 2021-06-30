package com.exadel.discount.platform.model.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
public class SubCategoryDto {
    @NotBlank
    @Size(min = 3, max = 20)
    private String name;
    @NotNull
    private UUID categoryId;
    @ToString.Exclude
    private List<DiscountDto> discounts;
}
