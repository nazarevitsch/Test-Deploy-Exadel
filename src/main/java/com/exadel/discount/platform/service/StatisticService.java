package com.exadel.discount.platform.service;

import com.exadel.discount.platform.converter.VendorMapper;
import com.exadel.discount.platform.mapper.CategoryMapper;
import com.exadel.discount.platform.mapper.DiscountMapper;
import com.exadel.discount.platform.mapper.SubCategoryMapper;
import com.exadel.discount.platform.model.dto.DiscountDtoResponse;
import com.exadel.discount.platform.model.dto.MainStatisticDtoResponse;
import com.exadel.discount.platform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final DiscountRepository discountRepository;
    private final UsedDiscountCustomRepository usedDiscountCustomRepository;
    private final UsedDiscountRepository usedDiscountRepository;
    private final DiscountMapper discountMapper;
    private final VendorMapper vendorMapper;
    private final CategoryMapper categoryMapper;
    private final SubCategoryMapper subCategoryMapper;
    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public MainStatisticDtoResponse getMainStatistic() {
        MainStatisticDtoResponse mainStatisticDtoResponse = new MainStatisticDtoResponse();

        mainStatisticDtoResponse.setTheMostPopularDiscount(discountMapper.entityToDto(discountRepository.findMaxUsedDiscount()));

        System.out.println(usedDiscountRepository.findTopByOrderByUsageDateDesc().getId());

        mainStatisticDtoResponse.setLastEndingDiscount(discountMapper.entityToDto(discountRepository.findTopByOrderByEndDateDesc()));

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        mainStatisticDtoResponse.setActiveDiscountsSize(discountRepository.countAllByStartDateBeforeAndEndDateAfter(zonedDateTime, zonedDateTime));

        mainStatisticDtoResponse.setDiscountSize(discountRepository.countAllBy());

        mainStatisticDtoResponse.setVendorSize(vendorRepository.countAllBy());

        mainStatisticDtoResponse.setUserSize(userRepository.countAllBy());

        mainStatisticDtoResponse.setTheMostPopularVendor(vendorMapper.entityToResponseDto(vendorRepository.getTheBestVendor()));

        mainStatisticDtoResponse.setTheMostPopularCategory(categoryMapper.entityToResponseDto(categoryRepository.getTheBestCategory()));

        mainStatisticDtoResponse.setTheMostPopularSubCategory(subCategoryMapper.entityToResponseDto(subCategoryRepository.getTheBestSubCategory()));

        return mainStatisticDtoResponse;
    }

    public Page<DiscountDtoResponse> getUserHistory(int page, int size, ZonedDateTime startDate, ZonedDateTime endDate) {
        return discountMapper.usedDiscountToDiscountDtoResponse(usedDiscountCustomRepository.
                findAllByFilters(startDate, endDate, PageRequest.of(page, size)));
    }
}
