package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {
    List<Vendor> findAllByDeleted(boolean isDeleted);
}
