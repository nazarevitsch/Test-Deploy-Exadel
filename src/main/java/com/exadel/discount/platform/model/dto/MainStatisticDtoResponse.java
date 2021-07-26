package com.exadel.discount.platform.model.dto;

import lombok.Data;

@Data
public class MainStatisticDtoResponse {

    private String mostPopularCity;

    private String mostPopularCountry;

    private int userSize;//

    private int amountOfUsedDiscount;//

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
