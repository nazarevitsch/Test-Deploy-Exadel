package com.exadel.discount.platform.mapper;

import com.exadel.discount.platform.model.FavoriteDiscount;
import com.exadel.discount.platform.model.dto.FavoriteDiscountResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FavoriteDiscountMapper {

    private ModelMapper mapper;

    public FavoriteDiscountMapper() {
        mapper = new ModelMapper();
    }

    public FavoriteDiscountResponseDto entityToResponseDto(FavoriteDiscount favoriteDiscount) {
        return mapper.map(favoriteDiscount, FavoriteDiscountResponseDto.class);
    }
}
