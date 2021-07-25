package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.model.Category;
import com.exadel.discount.platform.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findAllByDeleted(boolean isDeleted);

    @Query(value = "select  *, sum(d_usage_count) over (partition by c_id) as sum from category\n" +
            "    join discount on c_id = d_category_id order by sum desc limit 1", nativeQuery = true)
    Category getTheBestCategory();
}
