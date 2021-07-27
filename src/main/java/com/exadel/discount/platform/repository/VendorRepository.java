package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {
    List<Vendor> findAllByDeleted(boolean isDeleted);

    int countAllBy();

    @Query(value = "select  *, sum(d_usage_count) over (partition by v_id) as sum from vendor " +
            "join discount on v_id = d_vendor_id order by sum desc limit 1", nativeQuery = true)
    Vendor getTheBestVendor();

    @Query(value = "select distinct v_id, v_deleted, v_email, v_name, v_description, v_image from " +
            "(select distinct *, sum(d_usage_count) over (partition by v_id) as sum from vendor " +
            "            join discount on v_id = d_vendor_id order by sum desc limit :size) as first", nativeQuery = true)
    List<Vendor> getBestVendors(@Param("size") int size);

    @Query(value = "select distinct sum(d_usage_count) over (partition by v_id) as sum from vendor " +
            "join discount on v_id = d_vendor_id where d_vendor_id = :vendor_id", nativeQuery = true)
    int getSumOfDiscountUsageForVendorById(@Param("vendor_id") UUID id);
}
