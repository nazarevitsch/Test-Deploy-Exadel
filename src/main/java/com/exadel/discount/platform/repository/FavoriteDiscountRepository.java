package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.model.FavoriteDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FavoriteDiscountRepository extends JpaRepository<FavoriteDiscount, UUID> {

    Optional<FavoriteDiscount> findByDiscountIdAndUserId(UUID discountId, UUID userId);
    boolean existsByDiscountIdAndUserId(UUID discountId, UUID userId);
}
