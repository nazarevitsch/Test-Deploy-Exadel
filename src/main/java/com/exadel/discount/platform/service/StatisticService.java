package com.exadel.discount.platform.service;

import com.exadel.discount.platform.converter.VendorMapper;
import com.exadel.discount.platform.domain.MyUserDetails;
import com.exadel.discount.platform.mapper.CategoryMapper;
import com.exadel.discount.platform.mapper.DiscountMapper;
import com.exadel.discount.platform.mapper.SubCategoryMapper;
import com.exadel.discount.platform.model.VendorLocation;
import com.exadel.discount.platform.model.dto.MainStatisticDtoResponse;
import com.exadel.discount.platform.model.dto.UsedDiscountDtoResponse;
import com.exadel.discount.platform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

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
    private final VendorLocationRepository vendorLocationRepository;

    public MainStatisticDtoResponse getMainStatistic() {
        MainStatisticDtoResponse mainStatisticDtoResponse = new MainStatisticDtoResponse();

        mainStatisticDtoResponse.setTheMostPopularDiscount(discountMapper.entityToDto(discountRepository.findMaxUsedDiscount()));

        mainStatisticDtoResponse.setLastUsedDiscountDate(usedDiscountRepository.findTopByOrderByUsageDateDesc().getUsageDate());

        mainStatisticDtoResponse.setLastEndingDiscountDate(discountRepository.findTopByOrderByEndDateDesc().getEndDate());

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        mainStatisticDtoResponse.setActiveDiscountsSize(discountRepository.countAllByStartDateBeforeAndEndDateAfter(zonedDateTime, zonedDateTime));

        mainStatisticDtoResponse.setDiscountSize(discountRepository.countAllBy());

        mainStatisticDtoResponse.setVendorSize(vendorRepository.countAllBy());

        mainStatisticDtoResponse.setUserSize(userRepository.countAllBy());

        mainStatisticDtoResponse.setTheMostPopularVendor(vendorMapper.entityToResponseDto(vendorRepository.getTheBestVendor()));

        mainStatisticDtoResponse.setTheMostPopularCategory(categoryMapper.entityToResponseDto(categoryRepository.getTheBestCategory()));

        mainStatisticDtoResponse.setTheMostPopularSubCategory(subCategoryMapper.entityToResponseDto(subCategoryRepository.getTheBestSubCategory()));

        mainStatisticDtoResponse.setAmountOfUsedDiscount(usedDiscountRepository.findAmountOfUsedDiscount());

        VendorLocation bestVendorLocation = vendorLocationRepository.getTheBestVendorLocation();
        mainStatisticDtoResponse.setMostPopularCity(bestVendorLocation.getCity());
        mainStatisticDtoResponse.setMostPopularCountry(bestVendorLocation.getCountry());

        return mainStatisticDtoResponse;
    }

    public Page<UsedDiscountDtoResponse> getUserHistory(int page, int size, ZonedDateTime startDate, ZonedDateTime endDate) {
        return discountMapper.usedDiscountToUsedDiscountDtoResponse(usedDiscountCustomRepository.
                findAllByFilters(startDate, endDate,
                        ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId(),
                        null, null, null, null, null,
                        PageRequest.of(page, size)
                ));
    }

    public Page<UsedDiscountDtoResponse> getUsedDiscountHistory(ZonedDateTime startDate, ZonedDateTime endDate,
                                                            UUID categoryId, UUID subCategoryId, UUID vendorId, UUID userId,
                                                            String country, String city) {
        return discountMapper.usedDiscountToUsedDiscountDtoResponse(usedDiscountCustomRepository.findAllByFilters(
                startDate, endDate, userId, categoryId, subCategoryId, vendorId, country, city, null));
    }
}
