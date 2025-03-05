package com.backend.utils;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class CriteriaApiUtils {
    @SuppressWarnings("unchecked")
    public static <X, Y> Join<X, Y> getOrCreateJoin(From<?, X> root, SingularAttribute<? super X, Y> attr,
            JoinType type) {
        return (Join<X, Y>) root.getJoins().stream()
                .filter(j -> j.getAttribute().getName() == attr.getName() && j.getJoinType() == type)
                .findFirst()
                .orElseGet(() -> root.join(attr, type));
    }

    @SuppressWarnings("unchecked")
    public static <X, Y> Join<X, Y> getOrCreateJoin(From<?, X> root, ListAttribute<? super X, Y> attr,
            JoinType type) {
        return (Join<X, Y>) root.getJoins().stream()
                .filter(j -> j.getAttribute().getName() == attr.getName() && j.getJoinType() == type)
                .findFirst()
                .orElseGet(() -> root.join(attr, type));
    }

    public static Order buildOrder(Sort.Order order, Expression<?> exp, CriteriaBuilder builder) {
        return order.getDirection() == Direction.ASC
                ? builder.asc(exp)
                : builder.desc(exp);
    }
}
