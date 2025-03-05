package com.backend.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseEmailContext {
    private String to;
    private String from;
    private String subject;
    private String template;
}
