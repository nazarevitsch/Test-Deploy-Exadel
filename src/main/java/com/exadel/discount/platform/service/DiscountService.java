package com.exadel.discount.platform.service;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.exception.UnacceptableDiscountDtoException;
import com.exadel.discount.platform.model.dto.DiscountDto;
import com.exadel.discount.platform.repository.DiscountRepository;
import com.exadel.discount.platform.repository.SubCategoryRepository;
import com.exadel.discount.platform.repository.VendorLocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public void save(DiscountDto discountDto){
        log.info(discountDto.toString());

        Discount discount = discountDto.getDiscount();
        if (!discountDto.isOnline()) {
            if (discountDto.getLocations() == null || discountDto.getLocations().isEmpty()){
                throw new UnacceptableDiscountDtoException("Discount hasn't full data!");
            }
            discount.setVendorLocations(vendorLocationRepository.findAllById(discountDto.getLocations()));
        }
        if (discountDto.getSubCategories() == null || discount.getSubCategories().isEmpty()){
            throw new UnacceptableDiscountDtoException("Discount hasn't full data!");
        }
        discount.setSubCategories(subCategoryRepository.findAllById(discountDto.getSubCategories()));

        Discount discountWithID = discountRepository.save(discount);

        log.info(discountWithID.toString());
    }

    public Discount findById(UUID id){
        Optional<Discount> discount = Optional.of(discountRepository.getById(id));
        return discount.orElseThrow(()-> new NotFoundException("Discount wasn't found."));
    }


    public Page<Discount> findAllByFilters(int page, int size, UUID categoryId, List<UUID> subCategoriesIds,
                                           List<UUID> vendorIds, String country, String city, String searchWord) {
        searchWord = "^.*" + searchWord + ".*$";
        return discountRepository.findAllByFilters(vendorIds, categoryId, subCategoriesIds, country, city, searchWord,
                PageRequest.of(page, size));
    }
}
