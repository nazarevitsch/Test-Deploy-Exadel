package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.model.Category;
import com.exadel.discount.platform.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findAllByDeleted(boolean isDeleted);

    @Query(value = "select  *, sum(d_usage_count) over (partition by c_id) as sum from category\n" +
            "    join discount on c_id = d_category_id order by sum desc limit 1", nativeQuery = true)
    Category getTheBestCategory();

    @Query(value = "select distinct c_id, c_deleted, c_name from " +
            "(select distinct *, sum(d_usage_count) over (partition by c_id) as sum from category " +
            "join discount on c_id = d_category_id order by sum desc limit :size) as first;", nativeQuery = true)
    List<Category> getBestCategories(@Param("size") int size);

    @Query(value = "select distinct sum(d_usage_count) over (partition by c_id) as sum from category " +
            "join discount on c_id = d_category_id where d_category_id = :category_id", nativeQuery = true)
    int getSumOfDiscountUsageForCategoryById(@Param("category_id") UUID id);
}
