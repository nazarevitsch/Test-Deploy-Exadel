package com.exadel.discount.platform.service;

import com.exadel.discount.platform.domain.UsedDiscount;
import com.exadel.discount.platform.mapper.DiscountMapper;
import com.exadel.discount.platform.model.dto.DiscountDtoResponse;
import com.exadel.discount.platform.model.dto.MainStatisticDtoResponse;
import com.exadel.discount.platform.repository.DiscountRepository;
import com.exadel.discount.platform.repository.UsedDiscountCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final DiscountRepository discountRepository;
    private final UsedDiscountCustomRepository usedDiscountCustomRepository;
    private final DiscountMapper discountMapper;

    public MainStatisticDtoResponse getMainStatistic() {
        discountRepository.findMaxUsedDiscount();

        return null;
    }

    public Page<DiscountDtoResponse> getUserHistory(int page, int size) {
        Page<UsedDiscount> usedDiscounts = usedDiscountCustomRepository.findAllByFilters(PageRequest.of(page, size));
        return discountMapper.usedDiscountToDiscountDtoResponse(usedDiscounts);
    }
}
