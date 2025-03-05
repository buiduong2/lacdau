package com.backend.utils;

import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.backend.entities.Address;
import com.backend.entities.Address_;
import com.backend.entities.Customer;
import com.backend.entities.Customer_;
import com.backend.entities.Order_;
import com.backend.entities.Payment;
import com.backend.entities.Payment_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

@Component
public class CustomerOrderConverter extends AbstractOrderConverter<Customer> {

    @Override
    public Map<String, OrderSupplier<Customer>> getSuppliers() {
        return Map.of(
                "totalOrder", this::sortByTotalOrder,
                "totalPaymentAmountPaid", this::sortByTotalPaymentAmountPaid,
                "lastOrder", this::sortByLastOrder,
                "name", this::sortByName,
                "addressProvince", this::sortByProvinceName);
    }

    private Order sortByTotalOrder(Sort.Order order, Root<Customer> root, CriteriaQuery<?> query,
            CriteriaBuilder builder) {

        Join<Customer, com.backend.entities.Order> orderJoin = CriteriaApiUtils
                .getOrCreateJoin(root, Customer_.orders, JoinType.LEFT);

        return CriteriaApiUtils.buildOrder(order, builder.count(orderJoin.get(Order_.id)), builder);
    }

    private Order sortByProvinceName(Sort.Order order, Root<Customer> root, CriteriaQuery<?> query,
            CriteriaBuilder builder) {
        Join<Customer, Address> addressJoin = CriteriaApiUtils
                .getOrCreateJoin(root, Customer_.address, JoinType.LEFT);

        return CriteriaApiUtils.buildOrder(order, addressJoin.get(Address_.provinceName), builder);
    }

    private Order sortByTotalPaymentAmountPaid(Sort.Order order, Root<Customer> root, CriteriaQuery<?> query,
            CriteriaBuilder builder) {
        Join<Customer, com.backend.entities.Order> orderJoin = CriteriaApiUtils
                .getOrCreateJoin(root, Customer_.orders, JoinType.LEFT);

        Join<com.backend.entities.Order, Payment> paymentJoin = CriteriaApiUtils
                .getOrCreateJoin(orderJoin, Order_.payment, JoinType.LEFT);

        return CriteriaApiUtils.buildOrder(order, builder.sum(paymentJoin.get(Payment_.amountPaid)), builder);
    }

    private Order sortByLastOrder(Sort.Order order, Root<Customer> root, CriteriaQuery<?> query,
            CriteriaBuilder builder) {
        Join<Customer, com.backend.entities.Order> orderJoin = CriteriaApiUtils
                .getOrCreateJoin(root, Customer_.orders, JoinType.LEFT);
        return CriteriaApiUtils.buildOrder(order, builder.min(orderJoin.get(Order_.CREATED_AT)), builder);
    }

    private Order sortByName(Sort.Order order, Root<Customer> root, CriteriaQuery<?> query,
            CriteriaBuilder builder) {
        return CriteriaApiUtils.buildOrder(order,
                builder.concat(builder.concat(root.get(Customer_.firstName), " "), root.get(Customer_.lastName)),
                builder);
    }

}
