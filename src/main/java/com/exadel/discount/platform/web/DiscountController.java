package com.exadel.discount.platform.web;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.domain.Message;
import com.exadel.discount.platform.model.dto.DiscountDto;
import com.exadel.discount.platform.model.dto.DiscountDtoResponse;
import com.exadel.discount.platform.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping("/get_discounts")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'USER')")
    public ResponseEntity<Page<DiscountDtoResponse>> getAll(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "categoryId", required = false) UUID categoryId,
            @RequestParam(value = "subCategoriesIds", required = false) List<UUID> subCategoriesIds,
            @RequestParam(value = "vendorIds", required = false) List<UUID> vendorIds,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "searchWord", required = false) String searchWord
    ) {
        return new ResponseEntity<>(discountService.findAllByFilters(page, size, categoryId, subCategoriesIds, vendorIds,
                country, city, searchWord), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'USER')")
    public ResponseEntity<DiscountDtoResponse> getById(@PathVariable UUID id){
        return new ResponseEntity<>(discountService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<?> create(@RequestBody DiscountDto discountDto){
        discountService.save(discountDto);
        return new ResponseEntity<>(new Message("Discount was created!"), HttpStatus.OK);
    }
}
