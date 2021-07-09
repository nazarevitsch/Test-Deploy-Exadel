package com.exadel.discount.platform.mapper;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.model.dto.DiscountDtoResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<DiscountDtoResponse> map(Page<Discount> discount) {
       return discount.map(this::entityToDto);
    }
}
