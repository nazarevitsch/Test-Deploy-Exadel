package com.exadel.discount.platform.service;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.exception.UnacceptableDiscountDtoException;
import com.exadel.discount.platform.model.dto.DiscountDto;
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

    public void save(DiscountDto discountDto){
        log.info(discountDto.toString());

        Discount discount = discountDto.getDiscount();
        if (!discountDto.isOnline()) {
            if (discountDto.getLocations() == null || discountDto.getLocations().isEmpty()){
                throw new UnacceptableDiscountDtoException("Discount hasn't full data!");
            }
            discount.setVendorLocations(vendorLocationRepository.findAllById(discountDto.getLocations()));
        }
        if (discountDto.getSubCategories() == null || discountDto.getSubCategories().isEmpty()){
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


    public List<Discount> findAllByFilters(int page, int size, UUID categoryId, List<UUID> subCategoriesIds,
                                           List<UUID> vendorIds, String country, String city, String searchWord) {
        if (searchWord != null) {
            searchWord = "%" + searchWord + "%";
        }
        System.out.println(subCategoriesIds);
        System.out.println(vendorIds);
        System.out.println(categoryId);
        System.out.println(country);
        System.out.println(city);
        System.out.println(searchWord);
        return discountRepositoryCustom.findAllByFilters(vendorIds, categoryId, subCategoriesIds,
                country, city, searchWord, PageRequest.of(page, size));
    }
}
