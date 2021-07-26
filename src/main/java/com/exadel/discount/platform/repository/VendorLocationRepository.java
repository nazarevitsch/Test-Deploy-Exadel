package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.model.VendorLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorLocationRepository extends JpaRepository<VendorLocation, UUID> {

    Optional<VendorLocation> findByVendorIdAndId(UUID vendorId, UUID id);

    boolean existsByVendorIdAndId(UUID vendorId, UUID id);

    List<VendorLocation> findAllByDeleted(boolean isDeleted);

    @Query(value = "select  *, sum(d_usage_count) over (partition by v_id) as sum from vendor " +
            "join discount on v_id = d_vendor_id " +
            "join vendor_location on vendor.v_id = vendor_location.vendor_id order by sum limit 1", nativeQuery = true)
    VendorLocation getTheBestVendorLocation();
}
