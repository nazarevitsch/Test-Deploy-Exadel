package com.exadel.discount.platform.mapper;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.domain.MyUserDetails;
import com.exadel.discount.platform.domain.UsedDiscount;
import com.exadel.discount.platform.domain.User;
import com.exadel.discount.platform.model.dto.DiscountDto;
import com.exadel.discount.platform.model.dto.DiscountDtoResponse;
import com.exadel.discount.platform.model.dto.DiscountUpdateDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class DiscountMapper {
    final private ModelMapper modelMapper;

    public DiscountMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public DiscountDtoResponse entityToDto(Discount discount) {
        DiscountDtoResponse discountDtoResponse = modelMapper.map(discount, DiscountDtoResponse.class);
        if (discount.getLikedByUsers() != null) {
            for (User user : discount.getLikedByUsers()) {
                if (user.getId().equals(((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId())) {
                    discountDtoResponse.setLiked(true);
                }
            }
        }
        return discountDtoResponse;
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

    public Page<DiscountDtoResponse> usedDiscountToDiscountDtoResponse(Page<UsedDiscount> usedDiscounts) {
        return usedDiscounts.map((el) -> entityToDto(el.getDiscount()));
    }
}
