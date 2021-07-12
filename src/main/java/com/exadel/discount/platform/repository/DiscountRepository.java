package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.domain.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID>{

    @Query(value = "delete from discount_sub_category where dsc_discount_id = :discount_id and dsc_sub_category_id in :sub_category_ids",
    nativeQuery = true)
    @Modifying
    void removeSubCategoryRelationship(@Param("sub_category_ids") List<UUID> ids, @Param("discount_id") UUID id);

    @Query(value = "delete from discount_vendor_location where dvl_discount_id = :discount_id and dvl_vendor_location_id in :vendor_location_ids",
            nativeQuery = true)
    @Modifying
    void removeVendorLocationsRelationship(@Param("vendor_location_ids") List<UUID> ids, @Param("discount_id") UUID id);
}