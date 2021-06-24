package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.model.VendorLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VendorLocationRepository extends JpaRepository<VendorLocation, UUID> {
}
