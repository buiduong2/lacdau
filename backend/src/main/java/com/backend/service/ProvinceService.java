package com.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.dto.res.ProvinceDTO;
import com.backend.dto.res.ProvinceListDTO;
import com.backend.exception.ResourceNotFoundException;
import com.backend.mapper.ProvinceMapper;
import com.backend.repository.ProvinceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProvinceService {

    private final ProvinceRepository repository;
    private final ProvinceMapper mapper;

    public List<ProvinceListDTO> findAllListDTO() {
        return mapper.toListDTO(repository.findAll());
    }

    public ProvinceDTO findDTOById(long id) {
        return repository.findDTOById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Province not found"));
    }

}
