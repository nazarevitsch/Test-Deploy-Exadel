package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.domain.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
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

    void deleteAllByVendorId(UUID vendorId);

    Discount findDiscountByIdAndIsDeletedAndEndDateAfterAndStartDateBefore(UUID uuid, boolean deleted,
                                                                           ZonedDateTime zonedDateTime1, ZonedDateTime zonedDateTime2);

    @Modifying
    @Query(value = "update discount set d_usage_count = d_usage_count + 1 where d_id = :id", nativeQuery = true)
    void useDiscount(@Param("id") UUID id);



    @Query(value = "select * from discount where d_usage_count = (select max(d_usage_count) from discount)", nativeQuery = true)
    Discount findMaxUsedDiscount();
}
