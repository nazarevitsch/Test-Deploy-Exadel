package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.domain.Discount;
import com.exadel.discount.platform.domain.UsedDiscount;
import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.VendorLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
public class UsedDiscountCustomRepository {

    @Autowired
    private EntityManager entityManager;

    public Page<UsedDiscount> findAllByFilters(ZonedDateTime startDate, ZonedDateTime endDate,
                                               UUID userId, UUID categoryId, UUID subCategoryId, UUID vendorId,
                                               String country, String city, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UsedDiscount> criteriaQuery = criteriaBuilder.createQuery(UsedDiscount.class);
        Root<UsedDiscount> usedDiscountRoot = criteriaQuery.from(UsedDiscount.class);

        List<Predicate> predicates = new ArrayList<>();

        if (startDate != null) {
            Join<UsedDiscount, Discount> join1 = usedDiscountRoot.join("discount");
            Predicate predicateStartDate = criteriaBuilder.greaterThanOrEqualTo(join1.get("startDate"), startDate);
            predicates.add(predicateStartDate);
        }

        if (endDate != null) {
            Join<UsedDiscount, Discount> join2 = usedDiscountRoot.join("discount");
            Predicate predicateEndDate = criteriaBuilder.lessThanOrEqualTo(join2.get("endDate"), endDate);
            predicates.add(predicateEndDate);
        }

        if (userId != null) {
            Predicate predicateUserId = criteriaBuilder.equal(usedDiscountRoot.get("userId"), userId);
            predicates.add(predicateUserId);
        }

        if (vendorId != null) {
            Join<UsedDiscount, Discount> join3 = usedDiscountRoot.join("discount");
            Predicate predicateVendorId = criteriaBuilder.equal(join3.get("vendorId"), vendorId);
            predicates.add(predicateVendorId);
        }

        if (vendorId != null) {
            Join<UsedDiscount, Discount> join4 = usedDiscountRoot.join("discount");
            Predicate predicateCategoryId = criteriaBuilder.equal(join4.get("categoryId"), categoryId);
            predicates.add(predicateCategoryId);
        }

        if (country != null) {
            Join<UsedDiscount, Discount> join5 = usedDiscountRoot.join("discount");
            Join<Discount, SubCategory> join6 = join5.join("subCategories");
            Predicate predicateSubCategory = criteriaBuilder.equal(join6.get("id"), subCategoryId);
            predicates.add(predicateSubCategory);
        }

        if (country != null) {
            Join<UsedDiscount, Discount> join7 = usedDiscountRoot.join("discount");
            Join<Discount, VendorLocation> join8 = join7.join("vendorLocation");
            Predicate predicateCountry = criteriaBuilder.equal(join8.get("country"), country);
            predicates.add(predicateCountry);
            if (city != null) {
                Predicate predicateCity = criteriaBuilder.equal(join8.get("city"), city);
                predicates.add(predicateCity);
            }
        }

        if (predicates.size() != 0) {
            Predicate[] predicatesFinal = new Predicate[predicates.size()];
            predicates.toArray(predicatesFinal);
            criteriaQuery.where(predicatesFinal);
        }

        TypedQuery<UsedDiscount> query = entityManager.createQuery(criteriaQuery);
        int size = query.getResultList().size();
        if (pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }
        return new PageImpl<>(query.getResultList(), pageable != null ? pageable : PageRequest.of(0, 0), size);
    }
}
