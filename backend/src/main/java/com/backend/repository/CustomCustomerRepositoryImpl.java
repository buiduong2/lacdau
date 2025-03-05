package com.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Repository;

import com.backend.dto.req.FilterCustomerAdmin;
import com.backend.dto.res.CustomerAdminListDTO;
import com.backend.entities.Address;
import com.backend.entities.Address_;
import com.backend.entities.Customer;
import com.backend.entities.Customer_;
import com.backend.entities.Order;
import com.backend.entities.Order_;
import com.backend.entities.Payment;
import com.backend.entities.Payment_;
import com.backend.utils.CustomerOrderConverter;
import com.backend.utils.CustomerSpecs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class CustomCustomerRepositoryImpl implements CustomCustomerRepository {

    private final EntityManager em;

    private final CustomerSpecs customerSpecs;

    private final CustomerOrderConverter orderConverter;

    public CustomCustomerRepositoryImpl(JpaContext context, CustomerSpecs specs,
            CustomerOrderConverter orderConverter) {
        this.em = context.getEntityManagerByManagedType(Customer.class);
        this.customerSpecs = specs;
        this.orderConverter = orderConverter;
    }

    @Override
    public Page<CustomerAdminListDTO> findAdminAll(FilterCustomerAdmin filter, Pageable pageable) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CustomerAdminListDTO> query = builder.createQuery(CustomerAdminListDTO.class);
        Root<Customer> customer = query.from(Customer.class);

        Join<Customer, Order> order = customer.join(Customer_.orders, JoinType.LEFT);
        Join<Order, Payment> payment = order.join(Order_.payment, JoinType.LEFT);
        Join<Customer, Address> address = customer.join(Customer_.address, JoinType.LEFT);

        query.select(
                builder.construct(CustomerAdminListDTO.class,
                        customer.get(Customer_.id),
                        customer.get(Customer_.firstName),
                        customer.get(Customer_.lastName),
                        customer.get(Customer_.email),
                        builder.coalesce(builder.count(order.get(Order_.id)), 0L),
                        builder.coalesce(builder.sum(payment.get(Payment_.amountPaid)), 0L),
                        address.get(Address_.provinceName),
                        builder.min(order.get(Order_.CREATED_AT)),
                        customer.get(Customer_.createdAt),
                        customer.get(Customer_.updatedAt)

                )

        );

        query.groupBy(customer, address);

        query.orderBy(this.orderConverter.convert(pageable.getSort().toList(), customer, query, builder));

        Specification<Customer> spec = customerSpecs.byAdminFilter(filter);
        Predicate predicate = spec.toPredicate(customer, query, builder);
        if (predicate != null) {
            query.where(predicate);
        }

        List<CustomerAdminListDTO> list = em.createQuery(query)
                .setMaxResults(pageable.getPageSize())
                .setFirstResult((int) pageable.getOffset())
                .getResultList();

        return new PageImpl<>(list, pageable, getTotalItem(spec));
    }

    private long getTotalItem(Specification<Customer> spec) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Customer> customer = query.from(Customer.class);

        query.select(builder.count(customer));
        Predicate predicate = spec.toPredicate(customer, query, builder);
        if (predicate != null) {
            query.where(predicate);
        }

        return em.createQuery(query).getSingleResult();
    }

}
