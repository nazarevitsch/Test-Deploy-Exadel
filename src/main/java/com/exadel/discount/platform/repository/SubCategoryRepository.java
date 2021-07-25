package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubCategoryRepository extends JpaRepository<SubCategory, UUID> {

    void deleteAllByCategoryId(UUID id);

    List<SubCategory> findAllByCategoryIdAndDeleted(UUID categoryId, boolean isDeleted);

    Optional<SubCategory> findByCategoryIdAndId(UUID categoryId, UUID uuid);

    boolean existsByCategoryIdAndId(UUID categoryId, UUID uuid);

    @Query(value = "select  *, sum(d_usage_count) over (partition by c_id) as sum from category " +
            "join discount on c_id = d_category_id " +
            "join sub_category sc on c_id = sc.category_id order by sum desc limit 1", nativeQuery = true)
    SubCategory getTheBestSubCategory();

}