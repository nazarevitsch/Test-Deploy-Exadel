package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.domain.enums.SortingType;
import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.VendorLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class DiscountRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    public Page<Discount> findAllByFilters(List<UUID> vendorIds, UUID categoryId, List<UUID> subCategoriesIds,
                                           String country, String city, String searchWordRegularExpression,
                                           SortingType sortingType, Pageable pageable) {
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
        if (country != null) {
            Join<Discount, VendorLocation> join2 = discountRoot.join("vendorLocations");
            Predicate predicateLocationIsNotDeleted = criteriaBuilder.equal(join2.get("deleted"), false);
            predicates.add(predicateLocationIsNotDeleted);
            Predicate predicateCountry = criteriaBuilder.equal(join2.get("country"), country);
            predicates.add(predicateCountry);
            if (city != null) {
                Predicate predicateCity = criteriaBuilder.equal(join2.get("city"), city);
                predicates.add(predicateCity);
            }
        }
        if (searchWordRegularExpression != null) {
            Predicate predicateLikeSearchWord = criteriaBuilder.like(criteriaBuilder.upper(discountRoot.get("name")),
                    "%" + searchWordRegularExpression.toUpperCase() + "%");
            predicates.add(predicateLikeSearchWord);
        }

        Predicate deletedPredicate = criteriaBuilder.equal(discountRoot.get("isDeleted"), false);
        predicates.add(deletedPredicate);

        if (sortingType == SortingType.HOT_SALES) {
            criteriaQuery.orderBy(criteriaBuilder.desc(discountRoot.get("percentage")));
        }
        if (sortingType == SortingType.NEW_DISCOUNTS) {
            criteriaQuery.orderBy(criteriaBuilder.asc(discountRoot.get("startDate")));
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(discountRoot.get("startDate"), ZonedDateTime.now());
            predicates.add(predicate);
        }
        if (sortingType == SortingType.ENDING_SOON) {
            criteriaQuery.orderBy(criteriaBuilder.asc(discountRoot.get("endDate")));
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(discountRoot.get("startDate"), ZonedDateTime.now());
            predicates.add(predicate);
        }
        if (sortingType == SortingType.POPULAR) {
            criteriaQuery.orderBy(criteriaBuilder.desc(discountRoot.get("usageCount")));
        }
        if (sortingType == SortingType.COMING_SOON) {
            criteriaQuery.orderBy(criteriaBuilder.asc(discountRoot.get("startDate")));
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(discountRoot.get("startDate"), ZonedDateTime.now());
            predicates.add(predicate);
        }

        if (predicates.size() != 0) {
            Predicate[] predicatesFinal = new Predicate[predicates.size()];
            predicates.toArray(predicatesFinal);
            criteriaQuery.where(predicatesFinal);
        }

        TypedQuery<Discount> query = entityManager.createQuery(criteriaQuery);
        int size = query.getResultList().size();
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList(), pageable, size);
    }
}
