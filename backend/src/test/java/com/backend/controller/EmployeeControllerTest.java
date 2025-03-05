package com.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.backend.config.ResourceServerConfig;
import com.backend.entities.Employee;
import com.backend.repository.CustomerRepository;
import com.backend.repository.EmployeeRepository;
import com.backend.security.CustomJwtAuthenticationToken;
import com.backend.security.Permission;
import com.backend.service.EmployeeService;
import com.backend.utils.WithJwtAuthenticationToken;

@WebMvcTest(EmployeeController.class)
@Import(ResourceServerConfig.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeService service;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @Captor
    ArgumentCaptor<CustomJwtAuthenticationToken> jwtCaptor;

    @BeforeEach
    public void clearMock() {
        reset(service);
    }

    @Test
    @DisplayName("When check user has Profile is PermitAll()")
    void testExistsbyUserId() throws Exception {
        mvc.perform(get("/api/employees/check/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("WHen No Authenticated Should 401")
    void testGetProfile() throws Exception {
        mvc.perform(get("/api/employees"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("When Employee Authenticated should 200")
    @WithJwtAuthenticationToken(authorities = Permission.PRODUCT_MANAGE, principalType = Employee.class)
    void testGetProfile_withEmployee() throws Exception {
        mvc.perform(get("/api/employees"))
                .andExpect(status().isOk());

        verify(service).getProfile(jwtCaptor.capture());

        assertThat(jwtCaptor.getValue().getPerson()).isInstanceOf(Employee.class);
    }

    @Test
    @DisplayName("When Employee empty AUthorities should 403")
    @WithJwtAuthenticationToken(authorities = {}, principalType = Employee.class)
    void testGetProfile_withInValidEmployee() throws Exception {
        mvc.perform(get("/api/employees"))
                .andExpect(status().isForbidden());
    }

}
