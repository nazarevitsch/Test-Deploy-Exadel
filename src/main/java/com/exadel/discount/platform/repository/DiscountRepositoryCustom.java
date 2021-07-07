package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.VendorLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public class DiscountRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    public Page<Discount> findAllByFilters(List<UUID> vendorIds, UUID categoryId, List<UUID> subCategoriesIds,
                                           String country, String city, String searchWordRegularExpression, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Discount> criteriaQuery = criteriaBuilder.createQuery(Discount.class);
        Root<Discount> discountRoot = criteriaQuery.from(Discount.class);

        List<Predicate> predicates = new ArrayList<>();

        if (categoryId != null) {
            Predicate predicateCategoryId = criteriaBuilder.equal(discountRoot.get("categoryId"), categoryId);
            predicates.add(predicateCategoryId);
        }
        if (vendorIds != null) {
            Expression<UUID> vendors = discountRoot.get("vendorId");
            Predicate predicateVendorIds = vendors.in(vendorIds);
            predicates.add(predicateVendorIds);
        }

        if (subCategoriesIds != null) {
            Join<Discount, SubCategory> join1 = discountRoot.join("subCategories");
            Expression<Object> subCategories = join1.get("id");
            Predicate predicateSubCategories = subCategories.in(subCategoriesIds);
            predicates.add(predicateSubCategories);
        }

        if (city != null && country != null) {
            Join<Discount, VendorLocation> join2 = discountRoot.join("vendorLocations");
            Predicate predicateCity = criteriaBuilder.equal(join2.get("city"), city);
            Predicate predicateCountry = criteriaBuilder.equal(join2.get("country"), country);
            predicates.add(predicateCity);
            predicates.add(predicateCountry);
        }

        if (searchWordRegularExpression != null) {
            Predicate predicateLikeSearchWord = criteriaBuilder.like(criteriaBuilder.upper(discountRoot.get("name")), searchWordRegularExpression.toUpperCase());
            predicates.add(predicateLikeSearchWord);
        }

        if (predicates.size() != 0) {
            Predicate[] predicatesFinal = new Predicate[predicates.size()];
            predicates.toArray(predicatesFinal);
            criteriaQuery.where(predicatesFinal);
        }

        TypedQuery<Discount> query = entityManager.createQuery(criteriaQuery);
        return new PageImpl<>(query.getResultList(), pageable, query.getResultList().size());
    }
}
