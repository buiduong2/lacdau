package com.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.dto.req.FilterCustomerAdmin;
import com.backend.dto.res.CustomerAdminListDTO;

public interface CustomCustomerRepository {
    Page<CustomerAdminListDTO> findAdminAll(FilterCustomerAdmin filter, Pageable pageable);

}
