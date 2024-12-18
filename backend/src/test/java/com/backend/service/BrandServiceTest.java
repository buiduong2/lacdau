package com.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.backend.dto.req.BrandAdminUpdateDTO;
import com.backend.dto.res.BrandAdminBasicDTO;
import com.backend.entities.Brand_;
import com.backend.repository.TestBrandRepository;
import com.backend.utils.ServiceTest;

@ServiceTest
public class BrandServiceTest {

    @Autowired
    private BrandService brandService;

    @Autowired
    private TestBrandRepository repository;

    @Test
    void testCreate() {
        BrandAdminUpdateDTO dto = new BrandAdminUpdateDTO();
        dto.setName("New Brand");

        BrandAdminBasicDTO result = brandService.create(dto);

        assertThat(result)
                .isNotNull()
                .hasFieldOrProperty(Brand_.ID)
                .hasFieldOrPropertyWithValue(Brand_.NAME, "New Brand")
                .hasFieldOrProperty(Brand_.CREATED_AT)
                .hasFieldOrProperty(Brand_.UPDATED_AT);
        assertThat(repository.existsById(result.getId()));
    }
}
