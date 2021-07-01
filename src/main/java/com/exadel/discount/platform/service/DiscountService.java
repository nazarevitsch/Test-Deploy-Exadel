package com.exadel.discount.platform.service;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.dto.DiscountDto;
import com.exadel.discount.platform.repository.CategoryRepository;
import com.exadel.discount.platform.repository.DiscountRepository;
import com.exadel.discount.platform.repository.SubCategoryRepository;
import com.exadel.discount.platform.repository.VendorLocationRepository;
import liquibase.pro.packaged.A;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private VendorLocationRepository vendorLocationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    public void save(DiscountDto discountDto) {
        log.info(discountDto.toString());

        Discount discount = discountDto.getDiscount();
        if (!discountDto.isOnline()) {
            discount.setVendorLocations(vendorLocationRepository.findAllById(discountDto.getLocations()));
        }
        discount.setCategories(categoryRepository.findAllById(discountDto.getCategories()));
        discount.setSubCategories(subCategoryRepository.findAllById(discountDto.getSubCategories()));

        Discount discountWithID = discountRepository.save(discount);

        log.info(discountWithID.toString());
    }

    public Discount findById(UUID id){
        Optional<Discount> discount = Optional.of(discountRepository.getById(id));
        return discount.orElseThrow(()-> new NotFoundException("Discount wasn't found."));
    }

    public Page<Discount> findAll(int page, int size, List<UUID> categories, List<UUID> subCategories) {
        List<SubCategory> subList = subCategoryRepository.findAllById(subCategories == null ? new ArrayList<UUID>() : subCategories);
        return discountRepository.findAllBySubCategoriesIn(subList, PageRequest.of(page, size));
    }
}
