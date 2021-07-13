package com.exadel.discount.platform.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DiscountDtoResponse extends DiscountBaseDto {

    private List<VendorLocationResponseDto> vendorLocations;

    private List<SubCategoryResponseDto> subCategories;
}
