package com.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.dto.req.AttributeAdminCreateDTO;
import com.backend.dto.req.AttributeAdminUpdateDTO;
import com.backend.dto.res.AttributeAdminBasicDTO;
import com.backend.dto.res.AttributeAdminDTO;
import com.backend.entities.Attribute;
import com.backend.exception.ResourceNotFoundException;
import com.backend.mapper.AttributeMapper;
import com.backend.repository.AttributeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttributeService {

    private final AttributeRepository repository;

    private final AttributeMapper mapper;

    public List<AttributeAdminDTO> findAllAdmin() {
        return repository.findAllAdminBy();
    }

    private Attribute findWithAttrValById(long id) {
        return repository.findWithAttrValById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute WIth id =" + id + " is not found"));
    }

    public AttributeAdminBasicDTO findAdminBasicDTOById(long id) {
        return mapper.toBasicDTO(findWithAttrValById(id));
    }

    @Transactional
    public AttributeAdminBasicDTO create(AttributeAdminCreateDTO dto) {
        Attribute av = mapper.toEntity(dto);
        return mapper.toBasicDTO(repository.saveAndFlush(av));
    }

    @Transactional
    public AttributeAdminBasicDTO edit(AttributeAdminUpdateDTO dto, long id) {
        Attribute av = findWithAttrValById(id);
        mapper.updateEntity(av, dto);
        return mapper.toBasicDTO(repository.saveAndFlush(av));
    }

    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteByIdIn(List<Long> id) {
        repository.deleteAllById(id);
    }

}
