package com.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.dto.req.AttributeValueAdminUpdateDTO;
import com.backend.dto.res.AttributeValueAdminBasic;
import com.backend.dto.res.AttributeValueAdminDTO;
import com.backend.entities.AttributeValue;
import com.backend.exception.ResourceNotFoundException;
import com.backend.mapper.AttributeValueMapper;
import com.backend.repository.AttributeRepository;
import com.backend.repository.AttributeValueRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttributeValueService {
    private final AttributeValueRepository repository;

    private final AttributeRepository attributeRepository;

    private final AttributeValueMapper mapper;

    public List<AttributeValueAdminDTO> findAll() {
        return repository.findAllAdminDtosBy();
    }

    private AttributeValue findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AttributeValue with id = " + id + " is NOt found"));
    }

    public AttributeValueAdminBasic findAdminBasicDTOById(long id) {
        return mapper.toBasicDTO(findById(id));
    }

    @Transactional
    public AttributeValueAdminBasic create(AttributeValueAdminUpdateDTO dto) {
        AttributeValue attributeValue = mapper.toEntity(dto);
        if (dto.getAttributeId() != null) {
            attributeValue.setAttribute(attributeRepository.getReferenceById(dto.getAttributeId()));
        }
        repository.saveAndFlush(attributeValue);
        return mapper.toBasicDTO(attributeValue);
    }

    @Transactional
    public AttributeValueAdminBasic edit(AttributeValueAdminUpdateDTO dto, long id) {
        AttributeValue entity = findById(id);

        if (isAttributeNeedUpdate(entity, dto.getAttributeId())) {
            entity.setAttribute(attributeRepository.getReferenceById(dto.getAttributeId()));
        }
        repository.saveAndFlush(entity);
        return mapper.toBasicDTO(entity);
    }

    private boolean isAttributeNeedUpdate(AttributeValue entity, Long attributeId) {
        return (entity.getAttribute() == null && attributeId != null)
                || entity.getAttribute().getId() != attributeId;
    }

    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }

}
