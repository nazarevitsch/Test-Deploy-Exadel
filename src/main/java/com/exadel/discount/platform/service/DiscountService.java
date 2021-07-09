package com.exadel.discount.platform.service;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.exception.DeletedException;
import com.exadel.discount.platform.exception.DiscrepancyException;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.exception.UnacceptableDiscountDtoException;
import com.exadel.discount.platform.mapper.DiscountMapper;
import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.VendorLocation;
import com.exadel.discount.platform.model.dto.DiscountDto;
import com.exadel.discount.platform.model.dto.DiscountDtoResponse;
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
        Discount discount = discountDto.getDiscount();
        if (!discountDto.isOnline()) {
            if (discountDto.getLocations() == null || discountDto.getLocations().isEmpty()){
                throw new UnacceptableDiscountDtoException("Discount hasn't full data!");
            }
            List<VendorLocation> vendorLocations = vendorLocationRepository.findAllById(discountDto.getLocations());
            for (int i = 0; i < vendorLocations.size(); i++) {
                if (!vendorLocations.get(i).getVendor().getId().equals(discountDto.getVendorId())) throw new DiscrepancyException("Vendor location doesn't belong to vendor.");
                if (vendorLocations.get(i).isDeleted()) throw new DeletedException("Vendor location is deleted.");
            }
        }
        if (discountDto.getSubCategories() == null || discountDto.getSubCategories().isEmpty()){
            throw new UnacceptableDiscountDtoException("Discount hasn't full data!");
        }
        List<SubCategory> subCategories = subCategoryRepository.findAllById(discountDto.getSubCategories());
        for (int i = 0; i < subCategories.size(); i++) {
            if (!subCategories.get(i).getCategory().getId().equals(discountDto.getCategoryId())) throw new DiscrepancyException("Sub category doesn't belong to category.");
            if (subCategories.get(i).isDeleted()) throw new DeletedException("Sub category is deleted.");
        }
        discount.setSubCategories(subCategories);
        discountRepository.save(discount);
    }

    public DiscountDtoResponse findById(UUID id){
        Optional<Discount> discount = Optional.of(discountRepository.getById(id));
        return discountMapper.entityToDto(discount.orElseThrow(()-> new NotFoundException("Discount wasn't found.")));
    }

    public Page<DiscountDtoResponse> findAllByFilters(int page, int size, UUID categoryId, List<UUID> subCategoriesIds,
                                                      List<UUID> vendorIds, String country, String city, String searchWord) {
        return discountMapper.map(discountRepositoryCustom.findAllByFilters(vendorIds, categoryId, subCategoriesIds,
                country, city, searchWord), PageRequest.of(page, size));
    }
}
