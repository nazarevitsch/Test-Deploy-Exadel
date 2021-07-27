package com.exadel.discount.platform.service;

import com.exadel.discount.platform.converter.VendorMapper;
import com.exadel.discount.platform.domain.MyUserDetails;
import com.exadel.discount.platform.domain.UsedDiscount;
import com.exadel.discount.platform.mapper.CategoryMapper;
import com.exadel.discount.platform.mapper.DiscountMapper;
import com.exadel.discount.platform.mapper.SubCategoryMapper;
import com.exadel.discount.platform.model.VendorLocation;
import com.exadel.discount.platform.model.dto.*;
import com.exadel.discount.platform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public Page<StatisticResponseDto> getUsedDiscountHistory(ZonedDateTime startDate, ZonedDateTime endDate,
                                                            UUID categoryId, UUID subCategoryId, UUID vendorId, UUID userId,
                                                            String country, String city) {
        Page<UsedDiscount> a = usedDiscountCustomRepository.findAllByFilters(
                startDate, endDate, userId, categoryId, subCategoryId, vendorId, country, city, null);

        List<StatisticResponseDto> response = new ArrayList<>();

        for (int i = 0; i < a.toList().size(); i++) {
            StatisticResponseDto statisticResponseDto = new StatisticResponseDto();
            statisticResponseDto.setName(a.toList().get(i).getDiscount().getName());
            statisticResponseDto.setCategoryName(a.toList().get(i).getDiscount().getCategory().getName());
            statisticResponseDto.setCity(a.toList().get(i).getDiscount().getVendorLocations().get(0).getCity());
            statisticResponseDto.setUserEmail(a.toList().get(i).getUser().getEmail());
            statisticResponseDto.setVendorName(a.toList().get(i).getDiscount().getVendor().getName());
            statisticResponseDto.setCountry(a.toList().get(i).getDiscount().getVendorLocations().get(0).getCountry());
            response.add(statisticResponseDto);
        }


        return new PageImpl<>(response, PageRequest.of(0,1), response.size());
    }

    public StatisticDiagramResponseDto getBestEntities() {
        StatisticDiagramResponseDto response = new StatisticDiagramResponseDto();


        response.setBestDiscounts(discountMapper.map(discountRepository.findAmountMaxUsedDiscount(5)));


        List<VendorStatisticResponseDto> vendors = vendorMapper.map(vendorRepository.getBestVendors(5));
        for (int i = 0; i < vendors.size(); i++) {
            vendors.get(i).setUsageCount(vendorRepository.getSumOfDiscountUsageForVendorById(vendors.get(i).getId()));
        }
        response.setBestVendors(vendors);

        return response;
    };
}
