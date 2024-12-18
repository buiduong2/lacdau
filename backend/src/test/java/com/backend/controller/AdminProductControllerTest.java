package com.backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.backend.service.ImageCloudService;
import com.backend.service.ProductService;

@WebMvcTest(AdminProductController.class)
public class AdminProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ImageCloudService cloudImageSerivce;

    @Test
    void testEditById_whenBodyNotAnd_ShouldReturn400() throws Exception {
        // Mockito.when(productService.findAdminDtoBy(null, null)).then

        mvc.perform(
                put("/api/admin/products/test-id")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$['status']", Matchers.is(400)))
                .andExpect(jsonPath("$['error']", Matchers.is("Validation Error")))
                .andExpect(jsonPath("$['message']", Matchers.is("Input validation Failed")))
                .andExpect(jsonPath("$['errors']", Matchers.iterableWithSize(Matchers.greaterThan(0))));
    }

    @Test
    void testEditByIdWithAssociations() {

    }

    @Test
    void testFindAll() {

    }
}
