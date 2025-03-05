package com.backend.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.backend.dto.req.FilterCustomerAdmin;
import com.backend.entities.Address;
import com.backend.entities.Address_;
import com.backend.entities.Customer;
import com.backend.entities.Customer_;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

@Component
public class CustomerSpecs {

    public Specification<Customer> byAdminFilter(FilterCustomerAdmin filter) {

        List<Specification<Customer>> specs = new ArrayList<>();

        if (Strings.isNotBlank(filter.getEmail())) {
            specs.add(isEmailLike(filter.getEmail()));
        }

        if (Strings.isNotBlank(filter.getName())) {
            specs.add(isNameLike(filter.getName()));

        }

        if (Strings.isNotBlank(filter.getAddressProvince())) {
            specs.add(isProvinceLike(filter.getAddressProvince()));
        }

        return Specification.allOf(specs);
    }

    public Specification<Customer> isNameLike(String name) {
        String wildCard = "%" + name.toUpperCase() + "%";
        return (root, query, builder) -> {
            Expression<String> nameExp = builder.concat(
                    builder.concat(root.get(Customer_.lastName), " "),
                    root.get(Customer_.lastName));
            return builder.like(builder.upper(nameExp), wildCard);
        };
    }

    public Specification<Customer> isEmailLike(String email) {
        String wildCard = "%" + email.toUpperCase() + "%";

        return (root, query, builder) -> {

            return builder.like(builder.upper(root.get(Customer_.email)), wildCard);
        };
    }

    public Specification<Customer> isProvinceLike(String province) {
        String wildCard = "%" + province.toUpperCase() + "%";
        return (root, query, builder) -> {
            Join<Customer, Address> address = CriteriaApiUtils.getOrCreateJoin(root, Customer_.address, JoinType.LEFT);
            return builder.like(builder.upper(address.get(Address_.provinceName)), wildCard);
        };
    }
}
