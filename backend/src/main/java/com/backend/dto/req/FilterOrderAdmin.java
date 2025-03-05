package com.backend.dto.req;

import java.util.List;

import com.backend.entities.enums.OrderStage;
import com.backend.validation.EnumValue;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterOrderAdmin {

    private String id;

    @Valid
    private List<@EnumValue(enumClass = OrderStage.class) String> stages;
}
