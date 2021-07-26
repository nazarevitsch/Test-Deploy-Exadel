package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.domain.UsedDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsedDiscountRepository extends JpaRepository<UsedDiscount, UUID> {

    UsedDiscount findTopByOrderByUsageDateDesc();

    @Query(value = "select count(*) from used_discount", nativeQuery = true)
    int findAmountOfUsedDiscount();
}
