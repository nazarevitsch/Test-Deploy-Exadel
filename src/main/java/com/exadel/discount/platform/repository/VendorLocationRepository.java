package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.model.VendorLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VendorLocationRepository extends JpaRepository<VendorLocation, UUID> {
    List<VendorLocation> findAllByVendorIdAndDeleted(UUID vendorId, boolean isDeleted);
    Optional<VendorLocation> findByVendorIdAndId(UUID vendorId, UUID id);
    boolean existsByVendorIdAndId(UUID vendorId, UUID id);
}
