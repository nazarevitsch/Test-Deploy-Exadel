package com.exadel.discount.platform.model.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DiscountDtoResponse extends DiscountBaseDto {

    private UUID id;

    private List<VendorLocationResponseDto> vendorLocations;

    private List<SubCategoryResponseDto> subCategories;

    private CategoryResponseDto category;

    private VendorResponseDto vendor;

    private boolean isLiked;

    private int usageCount;
}
