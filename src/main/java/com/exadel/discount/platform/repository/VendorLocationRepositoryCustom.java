package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.model.VendorLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VendorLocationRepositoryCustom {
    private final EntityManager entityManager;

    public Page<VendorLocation> findAllByFilters(
            UUID vendorId, boolean isDeleted, String country, String city, Pageable pageable
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VendorLocation> criteriaQuery = criteriaBuilder.createQuery(VendorLocation.class);
        Root<VendorLocation> vendorLocationRoot = criteriaQuery.from(VendorLocation.class);

        List<Predicate> predicates = new ArrayList<>();

        Predicate predicateVendorId = criteriaBuilder.equal(vendorLocationRoot.get("vendorId"), vendorId);
        predicates.add(predicateVendorId);
        Predicate predicateIsDeleted = criteriaBuilder.equal(vendorLocationRoot.get("deleted"), isDeleted);
        predicates.add(predicateIsDeleted);

        if (country != null) {
            Predicate predicateCountry = criteriaBuilder.equal(vendorLocationRoot.get("country"), country);
            predicates.add(predicateCountry);
        }

        if (city != null) {
            Predicate predicateCity = criteriaBuilder.equal(vendorLocationRoot.get("city"), city);
            predicates.add(predicateCity);
        }

        if (predicates.size() != 0) {
            Predicate[] predicatesResult = new Predicate[predicates.size()];
            predicates.toArray(predicatesResult);
            criteriaQuery.where(predicatesResult);
        }

        TypedQuery<VendorLocation> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(query.getResultList(), pageable, query.getResultList().size());
    }
}
