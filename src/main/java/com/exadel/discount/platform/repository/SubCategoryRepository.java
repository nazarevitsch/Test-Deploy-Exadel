package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubCategoryRepository extends JpaRepository<SubCategory, UUID> {

    void deleteAllByCategoryId(UUID id);

    List<SubCategory> findAllByCategoryIdAndDeleted(UUID categoryId, boolean isDeleted);

    Optional<SubCategory> findByCategoryIdAndId(UUID categoryId, UUID uuid);

    boolean existsByCategoryIdAndId(UUID categoryId, UUID uuid);
}