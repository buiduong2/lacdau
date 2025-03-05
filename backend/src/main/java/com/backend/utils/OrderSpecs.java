package com.backend.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.backend.dto.req.FilterOrderAdmin;
import com.backend.dto.req.FilterOrderCustomer;
import com.backend.entities.Customer_;
import com.backend.entities.Order;
import com.backend.entities.Order_;
import com.backend.entities.enums.OrderStage;

import jakarta.persistence.criteria.JoinType;

@Component
public class OrderSpecs {

    public Specification<Order> isIdLike(String keyword) {
        return (root, query, builder) -> {
            String wildcard = "%" + keyword + "%";
            return builder.like(builder.function("CAST", String.class, root.get(Order_.id)), wildcard);
        };
    }

    public Specification<Order> isCustomerId(long customerId) {
        return (root, query, builder) -> {
            return builder.equal(root.get(Order_.customer).get(Customer_.id), customerId);
        };
    }

    public Specification<Order> isStatus(String status) {

        if (status.equals("pending")) {
            return (root, query, builder) -> {
                return builder.equal(root.get(Order_.stage), OrderStage.PENDING);
            };

        } else if (status.equals("canceled")) {
            return (root, query, builder) -> {
                return root.get(Order_.stage).in(OrderStage.CANCELED, OrderStage.FAILURE);
            };
        } else if (status.equals("completed")) {
            return (root, query, builder) -> {
                return builder.equal(root.get(Order_.stage), OrderStage.COMPLETED);
            };
        }

        return (root, query, builder) -> {
            return builder.not(root.get(Order_.stage).in(
                    OrderStage.CANCELED,
                    OrderStage.FAILURE,
                    OrderStage.PENDING,
                    OrderStage.COMPLETED));
        };
    }

    public Specification<Order> isOrderStageIn(Collection<OrderStage> stages) {
        return (root, query, builder) -> {
            return root.get(Order_.stage).in(stages);
        };
    }

    @SuppressWarnings("null")
    public Specification<Order> fetchGraph() {
        return (root, query, builder) -> {
            if (query.getResultType() != Long.class) {
                root.fetch(Order_.customer, JoinType.LEFT);
                root.fetch(Order_.payment, JoinType.LEFT);

            }

            return builder.conjunction();
        };
    }

    @SuppressWarnings("null")
    public Specification<Order> fetchCustomerGraph() {
        return (root, query, builder) -> {
            if (query.getResultType() != Long.class) {
                root.fetch(Order_.payment, JoinType.LEFT);
            }

            return builder.conjunction();
        };
    }

    public Specification<Order> byAdminFilter(FilterOrderAdmin filter) {
        List<Specification<Order>> specs = new ArrayList<>();
        specs.add(fetchGraph());

        if (Strings.isNotBlank(filter.getId())) {
            specs.add(isIdLike(filter.getId()));
        }

        if (filter.getStages() != null && !filter.getStages().isEmpty()) {
            specs.add(isOrderStageIn(filter.getStages().stream().map(OrderStage::valueOf).toList()));
        }

        return Specification.allOf(specs);
    }

    public Specification<Order> byCustomerFilter(FilterOrderCustomer filter, long customerId) {
        List<Specification<Order>> specs = new ArrayList<>();
        specs.add(isCustomerId(customerId));
        specs.add(fetchCustomerGraph());
        if (filter != null && !Strings.isBlank(filter.getStatus())) {
            specs.add(isStatus(filter.getStatus()));
        }

        return Specification.allOf(specs);
    }
}
