package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.domain.MyUserDetails;
import com.exadel.discount.platform.domain.UsedDiscount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsedDiscountCustomRepository {

    @Autowired
    private EntityManager entityManager;

    public Page<UsedDiscount> findAllByFilters(Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UsedDiscount> criteriaQuery = criteriaBuilder.createQuery(UsedDiscount.class);
        Root<UsedDiscount> usedDiscountRoot = criteriaQuery.from(UsedDiscount.class);

        List<Predicate> predicates = new ArrayList<>();

        Predicate predicateUserId = criteriaBuilder.equal(usedDiscountRoot.get("userId"),
                ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        predicates.add(predicateUserId);

        if (predicates.size() != 0) {
            Predicate[] predicatesFinal = new Predicate[predicates.size()];
            predicates.toArray(predicatesFinal);
            criteriaQuery.where(predicatesFinal);
        }

        TypedQuery<UsedDiscount> query = entityManager.createQuery(criteriaQuery);
        int size = query.getResultList().size();
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList(), pageable, size);
    }
}
