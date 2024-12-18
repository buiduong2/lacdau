package com.backend.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.springframework.test.util.ReflectionTestUtils;

import com.backend.dto.req.AttributeAdminCreateDTO;
import com.backend.dto.req.AttributeAdminCreateDTO.AttributeValueDTO;
import com.backend.entities.Attribute;
import com.backend.entities.AttributeValue;

public class AttributeMapperTest {

    private AttributeMapper delegate = new AttributeMapperImpl_();

    private AttributeMapper mapper = new AttributeMapperImpl();

    @BeforeEach
    public void setDelegate() {
        ReflectionTestUtils.setField(mapper, "delegate", delegate);
    }

    @Test
    void testToEntity_whenAddAttributeValue_mustUseAddMethod() {
        AttributeAdminCreateDTO dto = new AttributeAdminCreateDTO();
        List<AttributeValueDTO> avs = List.of(new AttributeValueDTO(), new AttributeValueDTO());
        dto.setAttributeValues(avs);

        try (MockedConstruction<Attribute> mockedConstructor = mockConstruction(Attribute.class);) {
            mapper.toEntity(dto);
            verify(mockedConstructor.constructed().get(0), times(avs.size()))
                    .addAttributeValue(any(AttributeValue.class));
        }

    }

    @Test
    void testToEntity_whenAddAttributeValue_mustPassReferecen() {
        AttributeAdminCreateDTO dto = new AttributeAdminCreateDTO();
        List<AttributeValueDTO> avs = List.of(new AttributeValueDTO(), new AttributeValueDTO());
        dto.setAttributeValues(avs);
        Attribute result = mapper.toEntity(dto);

        assertThat(result).isNotNull();
        assertThat(result.getAttributeValues())
                .isNotEmpty()
                .allMatch(av -> av.getAttribute() == result);
    }
}
