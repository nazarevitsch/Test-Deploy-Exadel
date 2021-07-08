package com.exadel.discount.platform.mapper;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.model.dto.DiscountDtoResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<DiscountDtoResponse> map(List<Discount> discount) {
        return discount.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
