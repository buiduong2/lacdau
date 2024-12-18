package com.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.dto.req.RelateGroupAdminUpdateDTO;
import com.backend.dto.res.RelateGroupAdminBasicDTO;
import com.backend.dto.res.RelateGroupAdminDTO;
import com.backend.dto.res.RelateGroupInfoDTO;
import com.backend.entities.RelateGroup;
import com.backend.exception.ResourceNotFoundException;
import com.backend.mapper.RelateGroupMapper;
import com.backend.repository.RelateGroupRepository;
import com.backend.repository.RelateInfoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RelateGroupService {

    private final RelateGroupRepository repository;

    private final RelateGroupMapper mapper;

    private final RelateInfoRepository relateInfoRepository;

    public List<RelateGroupAdminDTO> findAll() {
        return repository.findAllAdminDtosBy();
    }

    private RelateGroup findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RelateGroup With Id = " + id + " is not found"));
    }

    public RelateGroupInfoDTO findRelateInfosById(Long id) {
        RelateGroup relateGroup = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RelateGroup by id: " + id + " is not found"));

        return mapper.toInfoDTO(relateGroup);
    }

    public RelateGroupAdminBasicDTO findBasicById(long id) {
        return mapper.toBasicDTO(findById(id));
    }

    @Transactional
    public RelateGroupAdminBasicDTO create(RelateGroupAdminUpdateDTO dto) {
        RelateGroup relateGroup = mapper.toEntity(dto);
        repository.saveAndFlush(relateGroup);
        return mapper.toBasicDTO(relateGroup);
    }

    @Transactional
    public RelateGroupAdminBasicDTO editById(RelateGroupAdminUpdateDTO dto, long id) {
        RelateGroup relateGroup = findById(id);
        mapper.updateEntity(relateGroup, dto);
        repository.saveAndFlush(relateGroup);
        return mapper.toBasicDTO(relateGroup);
    }

    @Transactional
    public void deleteById(long id) {
        RelateGroup rg = findById(id);
        relateInfoRepository.deleteByRelateGroupId(id);
        repository.delete(rg);
    }

}
