package com.exadel.discount.platform.web;

import com.exadel.discount.platform.model.dto.FavoriteDiscountResponseDto;
import com.exadel.discount.platform.service.FavoriteDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/discount/{id}/favorite")
public class FavoriteDiscountController {

    private final FavoriteDiscountService favoriteDiscountService;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<FavoriteDiscountResponseDto> like(@PathVariable UUID id) {
        return new ResponseEntity<>(favoriteDiscountService.like(id), HttpStatus.OK);
    }

    @DeleteMapping()
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> unLike(@PathVariable UUID id) {
        favoriteDiscountService.unLike(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
