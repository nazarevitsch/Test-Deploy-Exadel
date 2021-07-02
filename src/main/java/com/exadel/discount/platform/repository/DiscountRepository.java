package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.model.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID>, PagingAndSortingRepository<Discount, UUID> {

    @Query(value = "select * from discount" +
            " join discount_sub_category on d_id = dsc_discount_id" +
            " join discount_vendor_location on d_id = dvl_discount_id" +
            " join vendor_location on dvl_vendor_location_id = vl_id" +
            " where (:vendor_ids is null or d_vendor_id in :vendor_ids)" +
            " and (:category_id is null or d_category_id = :category_id)" +
            " and (:subCategoriesIds is null or dsc_sub_category_id in :subCategoriesIds)" +
            " and (:country is null or (vl_country = :country and vl_city = :city))" +
            " and (:searchWord = null or (d_name ~* :search_word or d_full_description ~* :searchWord))",
            nativeQuery = true)
    Page<Discount> findAllByFilters(@Param("vendor_ids") List<UUID> vendorIds, @Param("category_id") UUID categoryId,
                                    @Param("sub_categories_ids") List<UUID> subCategoriesIds, @Param("country") String country,
                                    @Param("city") String city, @Param("search_word") String searchWordRegularExpression,
                                    Pageable pageable);
}
