package com.exadel.discount.platform.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class FavoriteDiscountResponseDto {

    @NotNull
    private UUID id;
    @NotNull
    private UUID discountId;
    @NotNull
    private UUID userId;
    @NotNull
    private ZonedDateTime likeDate;
}
