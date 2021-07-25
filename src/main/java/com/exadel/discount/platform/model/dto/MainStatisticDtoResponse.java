package com.exadel.discount.platform.model.dto;

import lombok.Data;

@Data
public class MainStatisticDtoResponse {

    private int userSize;//

    private DiscountDtoResponse lastUsedDiscount;//

    private int vendorSize;//

    private int discountSize;//

    private int activeDiscountsSize;//

    private DiscountDtoResponse lastEndingDiscount;//

    private DiscountDtoResponse theMostPopularDiscount;//

    private VendorResponseDto theMostPopularVendor;//

    private CategoryResponseDto theMostPopularCategory;//

    private SubCategoryResponseDto theMostPopularSubCategory;
}
