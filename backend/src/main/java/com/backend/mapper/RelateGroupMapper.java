package com.backend.mapper;

import java.util.List;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.backend.dto.req.RelateGroupAdminUpdateDTO;
import com.backend.dto.req.RelateGroupAdminUpdateDTO.RelateInfoDTO;
import com.backend.dto.res.RelateDTO;
import com.backend.dto.res.RelateGroupAdminBasicDTO;
import com.backend.dto.res.RelateGroupInfoDTO;
import com.backend.entities.RelateGroup;
import com.backend.entities.RelateInfo;
import com.backend.repository.RelateInfoRepository;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@DecoratedWith(RelateGroupMapperDecorator.class)
public abstract class RelateGroupMapper {

    @Autowired
    private RelateInfoRepository relateInfoRepository;

    @Mapping(target = "relateInfos", expression = "java( getRelateDtos(relateGroup) )")
    public abstract RelateGroupInfoDTO toInfoDTO(RelateGroup relateGroup);

    public List<RelateDTO> getRelateDtos(RelateGroup relateGroup) {
        return relateInfoRepository.findByRelateGroupId(relateGroup.getId());
    }

    public abstract RelateGroupAdminBasicDTO toBasicDTO(RelateGroup relateGroup);

    @ToEntity
    @Mapping(target = "relateInfos", ignore = true)
    public abstract RelateGroup toEntity(RelateGroupAdminUpdateDTO dto);

    @ToEntity
    @Mapping(target = "relateInfos", ignore = true)
    public abstract void updateEntity(@MappingTarget RelateGroup entity, RelateGroupAdminUpdateDTO dto);

    @ToEntity
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "relateGroup", ignore = true)
    public abstract void updateRelateInfo(@MappingTarget RelateInfo relateInfo, RelateInfoDTO dto);
}
