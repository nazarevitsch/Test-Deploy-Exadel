package com.exadel.discount.platform.mapper;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.model.dto.DiscountDto;
import com.exadel.discount.platform.model.dto.DiscountDtoResponse;
import com.exadel.discount.platform.model.dto.DiscountUpdateDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;


@Component
public class DiscountMapper {
    final private ModelMapper modelMapper;

    public DiscountMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public DiscountDtoResponse entityToDto(Discount discount) {
        return modelMapper.map(discount, DiscountDtoResponse.class);
    }

    public Page<DiscountDtoResponse> map(Page<Discount> discount) {
       return discount.map(this::entityToDto);
    }

    public Discount dtoToEntity(DiscountDto discountDto){
        return modelMapper.map(discountDto, Discount.class);
    }

    public Discount updateDtoToEntity(DiscountUpdateDto discountUpdateDto){
        return modelMapper.map(discountUpdateDto, Discount.class);
    }
}
