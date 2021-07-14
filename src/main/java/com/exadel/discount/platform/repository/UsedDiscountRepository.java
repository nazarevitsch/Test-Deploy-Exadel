package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.domain.UsedDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsedDiscountRepository extends JpaRepository<UsedDiscount, UUID> {
}
