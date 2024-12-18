package com.backend.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.backend.dto.req.AttributeAdminUpdateDTO;
import com.backend.entities.Attribute;
import com.backend.entities.AttributeValue;

public abstract class AttributeMapperDecorator implements AttributeMapper {

    @Autowired
    @Qualifier("delegate")
    private AttributeMapper delegate;

    @Override
    public void updateEntity(Attribute entity, AttributeAdminUpdateDTO dto) {
        delegate.updateEntity(entity, dto);
        if (dto.getRemoveAttributeValueId() != null) {
            for (Long id : dto.getRemoveAttributeValueId()) {
                AttributeValue av = new AttributeValue();
                av.setId(id);
                entity.removeAttributeValue(av);
            }
        }
    }
}
