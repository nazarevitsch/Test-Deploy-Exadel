package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.domain.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID>{

    @Query(value = "select * from discount" +
            " join discount_sub_category on d_id = dsc_discount_id" +
            " join discount_vendor_location on d_id = dvl_discount_id" +
            " join vendor_location on dvl_vendor_location_id = vl_id" +
            " where (?1 is null or d_vendor_id in ?1)" +
            " and (?2 is null or d_category_id = ?2)" +
            " and (?3 is null or (dsc_sub_category_id in ?3))" +
            " and (?4 is null or (vl_country = ?4 and vl_city = ?5))" +
            " and (?6 is null or (d_name ~* ?6 or d_full_description ~* ?6))",
            nativeQuery = true)
    Page<Discount> findAllByFilters(@Param("vendor_ids") List<UUID> vendorIds,
                                    @Param("category_id") UUID categoryId,
                                    @Param("sub_categories_ids") List<UUID> subCategoriesIds,
                                    @Param("country") String country,
                                    @Param("city") String city,
                                    @Param("search_word") String searchWordRegularExpression,
                                    Pageable pageable);
}
