package com.exadel.discount.platform.service;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.exception.DeletedException;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.exception.BadRequestException;
import com.exadel.discount.platform.mapper.DiscountMapper;
import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.VendorLocation;
import com.exadel.discount.platform.model.dto.DiscountDto;
import com.exadel.discount.platform.model.dto.DiscountDtoResponse;
import com.exadel.discount.platform.model.dto.DiscountUpdateDto;
import com.exadel.discount.platform.repository.DiscountRepository;
import com.exadel.discount.platform.repository.DiscountRepositoryCustom;
import com.exadel.discount.platform.repository.SubCategoryRepository;
import com.exadel.discount.platform.repository.VendorLocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final VendorLocationRepository vendorLocationRepository;
    private final DiscountRepositoryCustom discountRepositoryCustom;
    private final DiscountMapper discountMapper;


    public void save(DiscountDto discountDto){
        System.out.println(discountDto);
        Discount discount = discountMapper.dtoToEntity(discountDto);
        validateDiscount(discount, discountDto.getLocationIds(), discountDto.getSubCategoryIds());
        discountRepository.save(discount);
    }

    public DiscountDtoResponse findById(UUID id){
        Optional<Discount> discount = Optional.of(discountRepository.getById(id));
        return discountMapper.entityToDto(discount.orElseThrow(()-> new NotFoundException("Discount wasn't found.")));
    }

    public Page<DiscountDtoResponse> findAllByFilters(int page, int size, UUID categoryId, List<UUID> subCategoriesIds,
                                                      List<UUID> vendorIds, String country, String city, String searchWord) {
        return discountMapper.map(discountRepositoryCustom.findAllByFilters(vendorIds, categoryId, subCategoriesIds,
                country, city, searchWord, PageRequest.of(page, size)));
    }

    public DiscountDtoResponse updateDiscount(UUID id, DiscountUpdateDto discountUpdateDto) {
        Discount discount = Optional.of(discountRepository.getById(id))
                .orElseThrow(() -> new NotFoundException("Discount doesn't exist."));

        Discount newDiscount = discountMapper.updateDtoToEntity(discountUpdateDto);
        newDiscount.setId(id);
        newDiscount.setVendorId(discount.getVendorId());

        validateDiscount(newDiscount, discountUpdateDto.getLocationIds(), discountUpdateDto.getSubCategoryIds());

        List<UUID> subIds = discount.getSubCategories().stream().map(SubCategory::getId).collect(Collectors.toList());
        List<UUID> locationsIds = discount.getVendorLocations().stream().map(VendorLocation::getId).collect(Collectors.toList());
        discountRepository.removeSubCategoryRelationship(subIds, id);
        discountRepository.removeVendorLocationsRelationship(locationsIds, id);

        return discountMapper.entityToDto(discountRepository.save(newDiscount));
    }

    public void toArchive(UUID id) {
        if (discountRepository.existsById(id)){
            discountRepository.deleteById(id);
            return;
        }
        throw new NotFoundException("Discount with such id doesn't exist.");
    }

    private void validateDiscount(Discount discount, List<UUID> locationsIds, List<UUID> subCategoriesIds) {
        if (!discount.getStartDate().isBefore(discount.getEndDate())) {
            throw new BadRequestException("Duration of discount is unacceptable.");
        }
        if (!discount.getStartDate().toLocalDate().equals(LocalDate.now(discount.getStartDate().getZone()))) {
            if (discount.getStartDate().isBefore(ZonedDateTime.now())) {
                throw new BadRequestException("Duration of discount is unacceptable.");
            }
        }
        if (!discount.isOnline()) {
            if (locationsIds == null || CollectionUtils.isEmpty(locationsIds)) {
                throw new BadRequestException("Discount hasn't full data!");
            }
            List<VendorLocation> vendorLocations = vendorLocationRepository.findAllById(locationsIds);
            for (VendorLocation vl : vendorLocations) {
                if (!vl.getVendor().getId().equals(discount.getVendorId()))
                    throw new BadRequestException("Vendor location doesn't belong to vendor.");
                if (vl.isDeleted()) throw new DeletedException("Vendor location is deleted.");
            }
            discount.setVendorLocations(vendorLocations);
        }
        if (subCategoriesIds == null || CollectionUtils.isEmpty(subCategoriesIds)) {
            throw new BadRequestException("Discount hasn't full data!");
        }
        List<SubCategory> subCategories = subCategoryRepository.findAllById(subCategoriesIds);
        for (SubCategory sc : subCategories) {
            if (!sc.getCategory().getId().equals(discount.getCategoryId()))
                throw new BadRequestException("Sub category doesn't belong to category.");
            if (sc.isDeleted()) throw new DeletedException("Sub category is deleted.");
        }
        discount.setSubCategories(subCategories);
    }
}
