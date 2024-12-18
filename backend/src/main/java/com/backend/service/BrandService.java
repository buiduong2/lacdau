package com.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dto.req.BrandAdminUpdateDTO;
import com.backend.dto.res.BrandAdminBasicDTO;
import com.backend.dto.res.BrandAdminDTO;
import com.backend.entities.Brand;
import com.backend.exception.ResourceNotFoundException;
import com.backend.mapper.BrandMapper;
import com.backend.repository.BrandRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository repository;

    private final BrandMapper mapper;

    private Brand findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand with id =" + id + " is Not found"));
    }

    public List<BrandAdminDTO> findAllAdminDtos() {
        return repository.findAllAdminDtosBy();
    }

    @Transactional
    public BrandAdminBasicDTO create(BrandAdminUpdateDTO dto) {
        Brand brand = mapper.toEntity(dto);
        repository.save(brand);
        return mapper.toBasicDTO(brand);
    }

    @Transactional
    public BrandAdminBasicDTO update(BrandAdminUpdateDTO dto, long id) {
        Brand brand = findById(id);
        mapper.updateEntity(brand, dto);
        return mapper.toBasicDTO(brand);
    }

    public BrandAdminBasicDTO findBasicById(long id) {
        return mapper.toBasicDTO(findById(id));
    }

    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }

}
