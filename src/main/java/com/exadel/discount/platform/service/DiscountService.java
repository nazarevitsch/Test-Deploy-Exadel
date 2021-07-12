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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private VendorLocationRepository vendorLocationRepository;

    @Autowired
    private DiscountRepositoryCustom discountRepositoryCustom;

    @Autowired
    private DiscountMapper discountMapper;

    public void save(DiscountDto discountDto){
        Discount discount = discountMapper.dtoToEntity(discountDto);
        validateVendorLocationsAndSubCategory(discount, discountDto.getLocationIds(), discountDto.getSubCategoryIds());
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

    public void updateDiscount(UUID id, DiscountUpdateDto discountUpdateDto) {
        Discount discount = Optional.of(discountRepository.getById(id))
                .orElseThrow(() -> new NotFoundException("Discount doesn't exist."));

        List<UUID> subIds = discount.getSubCategories().stream().map(SubCategory::getId).collect(Collectors.toList());
        List<UUID> locationsIds = discount.getVendorLocations().stream().map(VendorLocation::getId).collect(Collectors.toList());
        discountRepository.removeSubCategoryRelationship(subIds, id);
        discountRepository.removeVendorLocationsRelationship(locationsIds, id);
        Discount newDiscount = discountMapper.updateDtoToEntity(discountUpdateDto);
        newDiscount.setVendorId(discount.getVendorId());
        validateVendorLocationsAndSubCategory(newDiscount, discountUpdateDto.getLocationIds(), discountUpdateDto.getSubCategoryIds());
        newDiscount.setId(id);
        discountRepository.save(newDiscount);
    }

    public void toArchive(UUID id) {
        if (discountRepository.existsById(id)){
            discountRepository.deleteById(id);
        }
        throw new NotFoundException("Discount with such id doesn't exist.");
    }

    private void validateVendorLocationsAndSubCategory(Discount discount, List<UUID> locationsIds, List<UUID> subCategoriesIds){
        if (!discount.isOnline()) {
            if (locationsIds == null || locationsIds.isEmpty()){
                throw new BadRequestException("Discount hasn't full data!");
            }
            List<VendorLocation> vendorLocations = vendorLocationRepository.findAllById(locationsIds);
            for (int i = 0; i < vendorLocations.size(); i++) {
                if (!vendorLocations.get(i).getVendor().getId().equals(discount.getVendorId())) throw new BadRequestException("Vendor location doesn't belong to vendor.");
                if (vendorLocations.get(i).isDeleted()) throw new DeletedException("Vendor location is deleted.");
            }
            discount.setVendorLocations(vendorLocations);
        }
        if (subCategoriesIds == null || subCategoriesIds.isEmpty()){
            throw new BadRequestException("Discount hasn't full data!");
        }
        List<SubCategory> subCategories = subCategoryRepository.findAllById(subCategoriesIds);
        for (int i = 0; i < subCategories.size(); i++) {
            if (!subCategories.get(i).getCategory().getId().equals(discount.getCategoryId())) throw new BadRequestException("Sub category doesn't belong to category.");
            if (subCategories.get(i).isDeleted()) throw new DeletedException("Sub category is deleted.");
        }
        discount.setSubCategories(subCategories);
    }
}
