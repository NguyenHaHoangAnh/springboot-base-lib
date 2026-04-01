package com.example.core.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public abstract class SpecificationBase<U> implements Specification<U> {

    private final SpecSearchCriteria criteria;

    public SpecificationBase(SpecSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<U> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {

        boolean isNull =
                criteria.getValue() == null ||
                        "''".equals(criteria.getValue().toString());

        String key = criteria.getKey();

        switch (criteria.getOperation()) {

            case EQUALITY:
                return isNull
                        ? cb.isNull(root.get(key))
                        : cb.equal(root.get(key), criteria.getValue());

            case NEGATION:
                return isNull
                        ? cb.isNotNull(root.get(key))
                        : cb.notEqual(root.get(key), criteria.getValue());

            case GREATER_THAN:
                return cb.greaterThan(root.get(key), criteria.getValue().toString());

            case LESS_THAN:
                return cb.lessThan(root.get(key), criteria.getValue().toString());

            case GREATER_THAN_EQUALS:
                return cb.greaterThanOrEqualTo(root.get(key), criteria.getValue().toString());

            case LESS_THAN_EQUALS:
                return cb.lessThanOrEqualTo(root.get(key), criteria.getValue().toString());

            case CONTAINS:
                return cb.like(cb.upper(root.get(key)),
                        "%" + criteria.getValue().toString().toUpperCase() + "%");

            case STARTS_WITH:
                return cb.like(cb.upper(root.get(key)),
                        criteria.getValue().toString().toUpperCase() + "%");

            case ENDS_WITH:
                return cb.like(cb.upper(root.get(key)),
                        "%" + criteria.getValue().toString().toUpperCase());

            case IN:
                CriteriaBuilder.In<Object> in = cb.in(root.get(key));
                for (String v : criteria.getValue().toString().split(",")) {
                    in.value(v);
                }
                return in;

            default:
                return null;
        }
    }
}