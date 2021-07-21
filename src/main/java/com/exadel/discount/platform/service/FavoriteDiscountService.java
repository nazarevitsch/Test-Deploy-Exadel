package com.exadel.discount.platform.service;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.domain.MyUserDetails;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.exception.ConflictException;
import com.exadel.discount.platform.mapper.FavoriteDiscountMapper;
import com.exadel.discount.platform.model.FavoriteDiscount;
import com.exadel.discount.platform.model.dto.FavoriteDiscountResponseDto;
import com.exadel.discount.platform.repository.DiscountRepository;
import com.exadel.discount.platform.repository.FavoriteDiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavoriteDiscountService {

    private final FavoriteDiscountRepository favoriteDiscountRepository;
    private final FavoriteDiscountMapper mapper;
    private final DiscountRepository discountRepository;


    public FavoriteDiscountResponseDto like(UUID discountId) {
        Discount discount = Optional.of(
                discountRepository.findDiscountByIdAndIsDeleted(discountId, false)
        ).orElseThrow(() -> new NotFoundException("Discount with id " + discountId
                + " doesn't exist or was finished", discountId, FavoriteDiscount.class));

        MyUserDetails details = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean alreadyExists = favoriteDiscountRepository.existsByDiscountIdAndUserId(discountId, details.getUserId());

        if (alreadyExists) {
            throw new ConflictException("Discount with id " + discountId + " was liked");
        }
        FavoriteDiscount favoriteDiscount = new FavoriteDiscount();
        favoriteDiscount.setDiscountId(discountId);
        favoriteDiscount.setUserId(details.getUserId());
        favoriteDiscount.setLikeDate(ZonedDateTime.now());
        return mapper.entityToResponseDto(favoriteDiscountRepository.save(favoriteDiscount));
    }

    public void unLike(UUID discountId) {
        MyUserDetails details = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FavoriteDiscount favoriteDiscount = favoriteDiscountRepository
                .findByDiscountIdAndUserId(discountId, details.getUserId())
                .orElseThrow(() -> new NotFoundException("Discount with id " + discountId +
                        " was not found in favorites", discountId, FavoriteDiscount.class));
        favoriteDiscountRepository.deleteById(favoriteDiscount.getId());

    }
}
