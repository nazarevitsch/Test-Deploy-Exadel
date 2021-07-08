package com.exadel.discount.platform.model.dto;

import com.exadel.discount.platform.model.Category;
import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.VendorLocation;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class DiscountDtoResponse {

    private UUID id;

    private String name;

    private List<VendorLocationResponseDto> vendorLocations;

    private List<SubCategoryResponseDto> subCategories;

    private CategoryResponseDto category;

    private UUID categoryId;

    private UUID vendorId;

    private String fullDescription;

    private boolean isOnline;

    private String imageLink;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private int percent;
}
