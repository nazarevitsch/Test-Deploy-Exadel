package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.model.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID>, PagingAndSortingRepository<Discount, UUID> {

    Page<Discount> findAllBySubCategoriesIn(List<SubCategory> ids, Pageable pageable);
}
