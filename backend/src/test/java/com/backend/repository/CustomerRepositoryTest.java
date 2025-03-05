package com.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.backend.dto.req.FilterCustomerAdmin;
import com.backend.dto.res.CustomerAdminListDTO;
import com.backend.entities.Address;
import com.backend.entities.Customer;
import com.backend.entities.Customer_;
import com.backend.utils.CustomerSpecs;

import jakarta.persistence.EntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ComponentScan(basePackages = "com.backend.repository", basePackageClasses = CustomerSpecs.class)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("Custom Criterial Query should Work when with no filters")
    void findAdminAll_withNoParamters() {

        Pageable pageable = PageRequest.of(1, 10,
                Sort.by(Direction.DESC, Customer_.FIRST_NAME));

        Page<CustomerAdminListDTO> pagesDtos = customerRepository.findAdminAll(new FilterCustomerAdmin(), pageable);

        String sql = """
                SELECT
                    new com.backend.dto.res.CustomerAdminListDTO(
                        c.firstName AS firstName,
                        c.lastName AS lastName,
                        c.email AS email,
                        COUNT(o.id) AS totalOrder,
                        SUM(p.amountPaid) AS totalPaymentAmountPaid,
                        a.province AS addressProvince,
                        MIN(o.createdAt) AS lastOrder,
                        c.createdAt AS createdAt,
                        c.updatedAt AS updatedAt
                    )
                FROM
                    Customer AS c
                    LEFT JOIN c.address AS a
                    LEFT JOIN c.orders AS o
                    LEFT JOIN o.payment AS p
                GROUP BY
                   c,a
                ORDER BY
                    c.firstName DESC
                    """;

        List<CustomerAdminListDTO> expected = em.createQuery(sql, CustomerAdminListDTO.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        assertThat(pagesDtos.getContent()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Custome Critarial should work with Filter")
    void testFindAdminAll_withFilter() {
        FilterCustomerAdmin filter = new FilterCustomerAdmin();
        filter.setEmail("DUong");
        filter.setName("Duong");
        filter.setAddressProvince("Duong");

        Pageable pageable = PageRequest.of(1, 10,
                Sort.by(Direction.DESC, Customer_.FIRST_NAME));

        Page<CustomerAdminListDTO> pagesDtos = customerRepository.findAdminAll(filter, pageable);

        String sql = """
                SELECT
                    new com.backend.dto.res.CustomerAdminListDTO(
                        c.firstName AS firstName,
                        c.lastName AS lastName,
                        c.email AS email,
                        COUNT(o.id) AS totalOrder,
                        SUM(p.amountPaid) AS totalPaymentAmountPaid,
                        a.province AS addressProvince,
                        MIN(o.createdAt) AS lastOrder,
                        c.createdAt AS createdAt,
                        c.updatedAt AS updatedAt
                    )
                FROM
                    Customer AS c
                    LEFT JOIN c.address AS a
                    LEFT JOIN c.orders AS o
                    LEFT JOIN o.payment AS p
                WHERE
                    UPPER(CONCAT(c.firstName, ' ' , c.lastName)) LIKE UPPER(?1)
                    AND UPPER(c.email) LIKE UPPER(?2)
                    AND UPPER(a.province) LIKE UPPER(?3)
                GROUP BY
                   c,a
                ORDER BY
                    c.firstName DESC
                    """;

        List<CustomerAdminListDTO> expected = em.createQuery(sql, CustomerAdminListDTO.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .setParameter(1, filter.getName())
                .setParameter(2, filter.getEmail())
                .setParameter(3, filter.getAddressProvince())
                .getResultList();

        assertThat(pagesDtos.getContent()).isEqualTo(expected);
    }

    @Test
    void testFindAdminById() {
        Customer customer = new Customer();
        Address address = new Address();
        customer.setAddress(address);

        customerRepository.save(customer);
        em.flush();
        em.clear();
        Customer actual = customerRepository.findAdminById(customer.getId()).get();
        em.clear();
        

        assertThat(actual).isNotNull();
        assertThat(actual.getAddress()).isNotNull();
        assertTrue(Hibernate.isInitialized(actual.getAddress()));

    }
}
