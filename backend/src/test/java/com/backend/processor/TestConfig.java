package com.backend.processor;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.backend.mapper.OrderMapper;
import com.backend.service.MailSenderService;
import com.backend.service.OrderLogService;

@ComponentScan(basePackages = "com.backend.processor")
@Configuration
public class TestConfig {

    @MockBean
    private OrderLogService orderLogService;

    @MockBean
    private MailSenderService mailSenderService;

    @MockBean
    private OrderMapper orderMapper;

}
