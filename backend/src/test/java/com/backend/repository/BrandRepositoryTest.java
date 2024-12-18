package com.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.backend.dto.res.BrandAdminDTO;
import com.backend.entities.Brand;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BrandRepositoryTest {

    @Autowired
    private TestBrandRepository repository;

    @Autowired
    private TestProductRepository productRepository;

    @Test
    void testFindAllAdminDtosBy() {
        List<BrandAdminDTO> dtos = repository.findAllAdminDtosBy();

        Map<Long, Long> countByBrandId = productRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(p -> Optional.ofNullable(p.getBrand()).map(Brand::getId).orElse(-1L),
                        Collectors.counting()));

        assertThat(dtos).isNotEmpty();
        assertThat(dtos).anyMatch(dto -> countByBrandId.containsKey(dto.getId()))
                .allMatch(dto -> {
                    if (countByBrandId.containsKey(dto.getId())) {
                        return countByBrandId.get(dto.getId()).equals(dto.getProductCount());
                    }
                    return true;
                });
    }
}
