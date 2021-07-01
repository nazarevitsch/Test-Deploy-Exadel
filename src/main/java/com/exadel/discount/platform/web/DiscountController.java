package com.exadel.discount.platform.web;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.domain.Message;
import com.exadel.discount.platform.model.dto.DiscountDto;
import com.exadel.discount.platform.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'USER')")
    public ResponseEntity<List<Discount>> getAll(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "subCategories", required = false) List<UUID> ids) {
        return new ResponseEntity<>(discountService.findAll(page, size, ids),
                HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'USER')")
    public ResponseEntity<Discount> getById(@PathVariable UUID id){
        return new ResponseEntity<>(discountService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<?> create(@RequestBody DiscountDto discountDto){
        discountService.save(discountDto);
        return new ResponseEntity<>(new Message("Discount was created!"), HttpStatus.OK);
    }
}
