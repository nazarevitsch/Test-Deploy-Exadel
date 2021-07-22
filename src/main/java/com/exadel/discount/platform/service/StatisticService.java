package com.exadel.discount.platform.service;

import com.exadel.discount.platform.model.dto.MainStatisticDtoResponse;
import com.exadel.discount.platform.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final DiscountRepository discountRepository;

    public MainStatisticDtoResponse getMainStatistic() {
        discountRepository.findMaxUsedDiscount();

        return null;
    }
}
