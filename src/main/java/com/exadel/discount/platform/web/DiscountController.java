package com.exadel.discount.platform.web;

import com.exadel.discount.platform.domain.Message;
import com.exadel.discount.platform.domain.enums.SortingType;
import com.exadel.discount.platform.model.dto.DiscountDto;
import com.exadel.discount.platform.model.dto.DiscountDtoResponse;
import com.exadel.discount.platform.model.dto.DiscountUpdateDto;
import com.exadel.discount.platform.model.dto.UserLoginDTO;
import com.exadel.discount.platform.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping("/use_discount/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'USER')")
    public ResponseEntity<?> useDiscount(@PathVariable UUID id) {
        discountService.useDiscount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

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
            @RequestParam(value = "searchWord", required = false) String searchWord,
            @RequestParam(value = "sortingType", required = false) SortingType sortingType
    ) {
        return new ResponseEntity<>(discountService.findAllByFilters(page, size, categoryId, subCategoriesIds, vendorIds,
                country, city, searchWord, sortingType), HttpStatus.OK);
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

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody DiscountUpdateDto discountUpdateDto){
        return new ResponseEntity<>(discountService.updateDiscount(id, discountUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity delete(@PathVariable UUID id) {
        discountService.toArchive(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
