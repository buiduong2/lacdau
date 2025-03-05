package com.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dto.req.CustomerRegisterDTO;
import com.backend.dto.req.FilterCustomerAdmin;
import com.backend.dto.res.CustomerAdminDTO;
import com.backend.dto.res.CustomerAdminListDTO;
import com.backend.dto.res.CustomerDTO;
import com.backend.entities.Customer;
import com.backend.entities.enums.PersonStatus;
import com.backend.exception.ResourceNotFoundException;
import com.backend.mapper.CustomerMapper;
import com.backend.repository.CustomerRepository;
import com.backend.security.CustomJwtAuthenticationToken;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;

    private final CustomerMapper mapper;

    public boolean existsByUserId(long userId) {
        return repository.existsByUserId(userId);
    }

    public CustomerDTO getProfile(CustomJwtAuthenticationToken authenticationToken) {
        return repository.findProfileByUserId(authenticationToken.getPerson().getUserId())
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Not found"));
    }

    @Transactional
    public CustomerDTO createProfile(CustomerRegisterDTO data, Long userId) {
        Customer customer = new Customer();
        mapper.updateEnitty(customer, data);
        customer.setStatus(PersonStatus.ACTIVE);
        customer.setUserId(userId);

        repository.saveAndFlush(customer);
        return mapper.toDTO(customer);
    }

    @Transactional
    public CustomerDTO updateProfile(CustomerRegisterDTO data, Long userId) {
        Customer customer = repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not have Profile"));
        mapper.updateEnitty(customer, data);
        repository.saveAndFlush(customer);
        return mapper.toDTO(customer);
    }

    public Page<CustomerAdminListDTO> findAdminAll(FilterCustomerAdmin filter, Pageable pageable) {
        return repository.findAdminAll(filter, pageable);
    }

    public CustomerAdminDTO findAdminById(long id) {
        return repository.findAdminById(id)
                .map(mapper::toAdminDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

}
