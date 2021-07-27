package com.exadel.discount.platform.service;

import com.exadel.discount.platform.domain.*;
import com.exadel.discount.platform.domain.enums.EmailType;
import com.exadel.discount.platform.domain.enums.SortingType;
import com.exadel.discount.platform.domain.enums.ValidationType;
import com.exadel.discount.platform.exception.DeletedException;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.exception.BadRequestException;
import com.exadel.discount.platform.mapper.DiscountMapper;
import com.exadel.discount.platform.model.Category;
import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.Vendor;
import com.exadel.discount.platform.model.VendorLocation;
import com.exadel.discount.platform.model.dto.DiscountDto;
import com.exadel.discount.platform.model.dto.DiscountDtoResponse;
import com.exadel.discount.platform.model.dto.DiscountUpdateDto;
import com.exadel.discount.platform.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZonedDateTime;
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
    private final UsedDiscountRepository usedDiscountRepository;
    private final EmailNotificationService emailNotificationService;
    private final UserDetailsService userDetailsService;
    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;

    public void useDiscount(UUID discountId) {
        ZonedDateTime now = ZonedDateTime.now();
        Discount discount = Optional.of(discountRepository.findDiscountByIdAndIsDeletedAndEndDateAfterAndStartDateBefore(discountId, false, now, now))
                .orElseThrow(() -> new NotFoundException("Discount with id "
                        + discountId + " doesn't exist or not active.", discountId, Discount.class));

        MyUserDetails details = ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        UsedDiscount usedDiscount = new UsedDiscount();
        usedDiscount.setDiscountId(discountId);
        usedDiscount.setUserId(details.getUserId());

        UsedDiscount usedDiscountSaved = usedDiscountRepository.save(usedDiscount);
        discountRepository.useDiscount(discountId);

        UserDetails userDetails = userDetailsService.findUserDetailsByUserId(details.getUserId());

        emailNotificationService.notifyVendorAboutUsageOfDiscount(discount.getVendor().getEmail(),
                discount, details, usedDiscountSaved, userDetails);

        emailNotificationService.notifyUserAboutUsageOfDiscount(discount.getVendor().getEmail(),
                discount, usedDiscountSaved, userDetails);
    }

    public void save(DiscountDto discountDto){
        Discount discount = discountMapper.dtoToEntity(discountDto);
        validateDiscount(discount, discountDto.getLocationIds(), discountDto.getSubCategoryIds(), ValidationType.CREATE);
        discountRepository.save(discount);
    }

    public DiscountDtoResponse findById(UUID id) {
        Optional<Discount> discount = Optional.of(discountRepository.getById(id));
        return discountMapper.entityToDto(discount.orElseThrow(() -> new NotFoundException("Discount with id " + id +
                " does not exist", id, Discount.class)));
    }

    public Page<DiscountDtoResponse> findAllByFilters(int page, int size, UUID categoryId, List<UUID> subCategoriesIds,
                                                      List<UUID> vendorIds, String country, String city, boolean isFavourite,
                                                      String searchWord, SortingType sortingType) {
        return discountMapper.map(discountRepositoryCustom.findAllByFilters(vendorIds, categoryId, subCategoriesIds,
                country, city, searchWord, isFavourite, sortingType, PageRequest.of(page, size)));
    }

    public DiscountDtoResponse updateDiscount(UUID id, DiscountUpdateDto discountUpdateDto) {
        Discount discount = Optional.of(discountRepository.getById(id))
                .orElseThrow(() -> new NotFoundException("Discount with id " + id + "does not exist", id, Discount.class));

        Discount newDiscount = discountMapper.updateDtoToEntity(discountUpdateDto);
        newDiscount.setId(id);
        newDiscount.setUsageCount(discount.getUsageCount());
        newDiscount.setVendorId(discount.getVendorId());
        if (discount.getStartDate().isBefore(ZonedDateTime.now())) {
            newDiscount.setStartDate(discount.getStartDate());
        }

        validateDiscount(newDiscount, discountUpdateDto.getLocationIds(), discountUpdateDto.getSubCategoryIds(), ValidationType.UPDATE);

        List<UUID> subIds = discount.getSubCategories().stream().map(SubCategory::getId).collect(Collectors.toList());
        List<UUID> locationsIds = discount.getVendorLocations().stream().map(VendorLocation::getId).collect(Collectors.toList());
        discountRepository.removeSubCategoryRelationship(subIds, id);
        discountRepository.removeVendorLocationsRelationship(locationsIds, id);

        return discountMapper.entityToDto(discountRepository.save(newDiscount));
    }

    public void toArchive(UUID id) {
        if (discountRepository.existsById(id)) {
            discountRepository.deleteById(id);
            return;
        }
        throw new NotFoundException("Discount with id " + id + " does not .", id, Discount.class);
    }

    private void validateDiscount(Discount discount, List<UUID> locationsIds, List<UUID> subCategoriesIds, ValidationType validationType) {
        if (!discount.getStartDate().isBefore(discount.getEndDate())) {
            throw new BadRequestException("End time of discount can't be before start time.", discount.getId(), Discount.class,
                    "endDate");
        }
        switch (validationType) {
            case CREATE:
                if (!discount.getStartDate().toLocalDate().equals(LocalDate.now(discount.getStartDate().getZone()))) {
                    if (discount.getStartDate().isBefore(ZonedDateTime.now())) {
                        throw new BadRequestException("Start time of discount should be at last today or later.",
                                discount.getId(), Discount.class, "startDate");
                    }
                }
                break;
            case UPDATE:
                if (discount.getEndDate().isBefore(ZonedDateTime.now())){
                    throw new BadRequestException("End time of discount should be later.",
                            discount.getId(), Discount.class, "endDate");
                }
                break;
            default:
                throw new IllegalArgumentException("Incorrect validation's type.");
        }
        Vendor vendor = Optional.of(vendorRepository.getById(discount.getVendorId()))
                .orElseThrow(() -> new NotFoundException("Vendor with id" + discount.getVendorId() + " wasn't found.",
                        discount.getVendorId(), Vendor.class));
        if (!discount.isOnline()) {
            if (locationsIds == null || CollectionUtils.isEmpty(locationsIds)) {
                throw new BadRequestException("If location/s of discount isn't online, it should have locations.",
                        discount.getId(), Discount.class, "vendorLocations");
            }
            List<VendorLocation> vendorLocations = vendorLocationRepository.findAllById(locationsIds);
            if (vendorLocations.size() != locationsIds.size()) {
                throw new BadRequestException("Id/s of location/s is/are incorrect.",
                        discount.getId(), Discount.class, "vendorLocations");
            }
            for (VendorLocation vl : vendorLocations) {
                if (!vl.getVendor().getId().equals(vendor.getId()))
                    throw new NotFoundException("Vendor location with id " + vl.getId() +
                            " does not exist.", vl.getId(), VendorLocation.class);
                if (vl.isDeleted())
                    throw new DeletedException("Vendor location with id " + vl.getId() +
                            " is deleted.", vl.getId(), VendorLocation.class);
            }
            discount.setVendorLocations(vendorLocations);
        }
        Category category = Optional.of(categoryRepository.getById(discount.getCategoryId()))
                .orElseThrow(() -> new NotFoundException("Category with id" + discount.getCategoryId() + " wasn't found.",
                discount.getCategoryId(), Category.class));
        if (subCategoriesIds == null || CollectionUtils.isEmpty(subCategoriesIds)) {
            throw new BadRequestException("Each discount should have sub categories.", discount.getId(), Discount.class,
                    "subCategories");
        }
        List<SubCategory> subCategories = subCategoryRepository.findAllById(subCategoriesIds);
        if (subCategories.size() != subCategoriesIds.size()) {
            throw new BadRequestException("Id/s of sub category/ies is/are incorrect.",
                    discount.getId(), Discount.class, "subCategoryLocations");
        }
        for (SubCategory sc : subCategories) {
            if (!sc.getCategory().getId().equals(category.getId()))
                throw new NotFoundException("SubCategory with id " + sc.getId() +
                        " does not exist.", sc.getId(), SubCategory.class);
            if (sc.isDeleted()) throw new DeletedException("Sub category with id " + sc.getId() +
                    " is deleted.", sc.getId(), SubCategory.class);
        }
        discount.setSubCategories(subCategories);
    }
}
