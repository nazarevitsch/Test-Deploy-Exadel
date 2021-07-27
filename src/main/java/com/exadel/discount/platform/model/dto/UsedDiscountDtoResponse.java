package com.exadel.discount.platform.model.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class UsedDiscountDtoResponse extends DiscountDtoResponse{

    private ZonedDateTime usageDate;

    private UserDtoResponse user;
}
