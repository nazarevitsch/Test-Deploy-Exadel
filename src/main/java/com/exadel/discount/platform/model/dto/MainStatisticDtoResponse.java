package com.exadel.discount.platform.model.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class MainStatisticDtoResponse {

    private String mostPopularCity;

    private String mostPopularCountry;

    private int userSize;

    private int amountOfUsedDiscount;

    private ZonedDateTime lastUsedDiscountDate;

    private int vendorSize;

    private int discountSize;

    private int activeDiscountsSize;

    private ZonedDateTime lastEndingDiscountDate;

    private DiscountDtoResponse theMostPopularDiscount;

    private VendorResponseDto theMostPopularVendor;

    private CategoryResponseDto theMostPopularCategory;

    private SubCategoryResponseDto theMostPopularSubCategory;
}
