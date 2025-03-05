package com.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dto.req.EmployeeRegisterDTO;
import com.backend.dto.res.EmployeeAdminDTO;
import com.backend.dto.res.EmployeeAdminListDTO;
import com.backend.dto.res.EmployeeDTO;
import com.backend.entities.Employee;
import com.backend.entities.enums.PersonStatus;
import com.backend.exception.ResourceNotFoundException;
import com.backend.mapper.EmployeeMapper;
import com.backend.repository.EmployeeRepository;
import com.backend.security.CustomJwtAuthenticationToken;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;

    private final EmployeeMapper mapper;

    public boolean existsByUserId(long userId) {
        return repository.existsByUserId(userId);
    }

    public EmployeeDTO getProfile(CustomJwtAuthenticationToken token) {
        return repository.findProfileByUserId(token.getPerson().getUserId())
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Not found"));
    }

    @Transactional
    public void createProfile(EmployeeRegisterDTO data, Long userId) {
        Employee employee = new Employee();
        mapper.updateEnitty(employee, data);
        employee.setStatus(PersonStatus.ACTIVE);
        employee.setUserId(userId);

        repository.saveAndFlush(employee);

    }

    @Transactional
    public EmployeeDTO updateProfile(EmployeeRegisterDTO data, Long userId) {
        Employee employee = repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not have Profile"));
        mapper.updateEnitty(employee, data);
        repository.saveAndFlush(employee);
        return mapper.toDTO(employee);
    }

    public List<EmployeeAdminListDTO> findAdminAll() {
        return mapper.toAdminListDTOs(repository.findAll());
    }

    public EmployeeAdminDTO findAdminById(long id) {
        return repository.findById(id)
                .map(mapper::toAdminDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

}
