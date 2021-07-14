package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.model.VendorLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VendorLocationRepository extends JpaRepository<VendorLocation, UUID> {
    Optional<VendorLocation> findByVendorIdAndId(UUID vendorId, UUID id);
    boolean existsByVendorIdAndId(UUID vendorId, UUID id);
    List<VendorLocation> findAllByDeleted(boolean isDeleted);
}
