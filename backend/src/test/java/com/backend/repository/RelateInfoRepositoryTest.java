package com.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.backend.dto.res.RelateDTO;
import com.backend.entities.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RelateInfoRepositoryTest {

    @Autowired
    private RelateInfoRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testFindBypPoductRelateGroupId() {
        Product product = productRepository.findByProductCode("BPK0005").get();

        List<RelateDTO> dtos = repository.findByRelateById(product.getRelateInfo().getId());
        assertThat(dtos).hasSize(3)
                .containsAll(List.of(
                        new RelateDTO(4, "BPK0005", "T21 TRẮNG", 190000),
                        new RelateDTO(3, "BPK0001", "G21 TRẮNG", 150000),
                        new RelateDTO(2, "BPK0002", "G21 ĐEN", 150000)));
    }
}
