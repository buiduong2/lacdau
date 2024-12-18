package com.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.backend.dto.req.RelateGroupAdminUpdateDTO;
import com.backend.dto.req.RelateGroupAdminUpdateDTO.RelateInfoDTO;
import com.backend.dto.res.RelateGroupAdminBasicDTO;
import com.backend.entities.Product;
import com.backend.entities.RelateGroup;
import com.backend.entities.RelateGroup_;
import com.backend.repository.RelateGroupRepository;
import com.backend.repository.RelateInfoRepository;
import com.backend.repository.TestProductRepository;
import com.backend.utils.ServiceTest;

@ServiceTest
public class RelateGroupServiceTest {

    @Autowired
    private RelateGroupService service;

    @Autowired
    public TestProductRepository productRepository;

    @Autowired
    public RelateGroupRepository repository;

    @Autowired
    private RelateInfoRepository relateInfoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testCreate_whenEmptyRelateInfo_shouldWork() {

        RelateGroupAdminUpdateDTO dto = new RelateGroupAdminUpdateDTO();
        dto.setName("NEW NAME");

        RelateGroupAdminBasicDTO info = service.create(dto);
        entityManager.flush();
        entityManager.clear();

        assertThat(info.getName()).isEqualTo("NEW NAME");
    }

    @Test
    void testCreate_whenHaveNewRelateInfo_ShouldCreate() {

        List<Product> products = productRepository.findFirst2ByRelateInfoIsNull();

        assertThat(products).isNotEmpty().hasSize(2).allMatch(p -> p.getRelateInfo() == null);
        entityManager.clear();

        List<RelateInfoDTO> infoDTOs = getInfoDTOs(products);

        RelateGroupAdminUpdateDTO dto = new RelateGroupAdminUpdateDTO();
        dto.setName("NEW NAME");
        dto.setRelateInfos(infoDTOs);
        entityManager.flush();
        entityManager.clear();

        RelateGroupAdminBasicDTO result = service.create(dto);

        entityManager.flush();
        entityManager.clear();

        Optional<RelateGroup> relateGroup = repository.findById(result.getId());

        assertThat(relateGroup).isNotEmpty();
        assertThat(relateGroup.get())
                .hasFieldOrPropertyWithValue(RelateGroup_.NAME, "NEW NAME");

        assertThat(relateGroup.get().getRelateInfos()).isNotEmpty()
                .allMatch(ri -> ri.getName().equals("New Info"))
                .allMatch(ri -> ri.getProduct().getId() != null);

    }

    // DKM hãy nhwos rằng ko phải chỉ có Transasction kết thúc thì nó mới flush. Khi
    // mà chúng ta sử dụng Query thì cũng flush luôn

    // Hoặc gặp 1 transaction khác cũng vậy

    @Test
    void testCreate_whenOverrideRelateInfo_ShouldWork() {
        List<Product> products = get2ProductHasRelateInfo();

        List<RelateInfoDTO> infoDTOs = getInfoDTOs(products);

        RelateGroupAdminUpdateDTO dto = new RelateGroupAdminUpdateDTO();
        dto.setName("NEW NAME");
        dto.setRelateInfos(infoDTOs);

        Long relateExistCount = relateInfoRepository.count();

        entityManager.clear();

        RelateGroupAdminBasicDTO result = service.create(dto);

        entityManager.flush();
        entityManager.clear();

        RelateGroup relateGroup = repository.findById(result.getId()).get();

        assertThat(relateExistCount).isEqualTo(relateInfoRepository.count());

        assertThat(relateGroup).isNotNull().hasFieldOrPropertyWithValue(RelateGroup_.NAME, "NEW NAME");

        assertThat(relateGroup.getRelateInfos())
                .hasSize(2)
                .allMatch(ri -> ri.getProduct().getId() != null)
                .allMatch(ri -> ri.getName().equals("New Info"));
    }

    private List<Product> get2ProductHasRelateInfo() {
        List<Product> products = productRepository.findFirst2ByRelateInfoIsNotNull();

        assertThat(products).hasSize(2).allMatch(product -> product.getRelateInfo().getId() != null);
        entityManager.clear();
        return products;
    }

    private List<RelateInfoDTO> getInfoDTOs(List<Product> products) {
        List<RelateInfoDTO> infoDTOs = products.stream().map(p -> {
            RelateInfoDTO infoDTO = new RelateInfoDTO();
            infoDTO.setName("New Info");
            infoDTO.setProductId(p.getProductCode());
            infoDTO.setPosition(1);
            return infoDTO;
        }).toList();
        return infoDTOs;
    }

    @Test
    void testEditById_mustRetainAll() {
        long productCount = productRepository.count();
        List<Product> products = get2ProductHasRelateInfo();
        List<RelateInfoDTO> infoDTOs = getInfoDTOs(products);
        RelateGroupAdminUpdateDTO dto = new RelateGroupAdminUpdateDTO();
        dto.setName("NEW NAME");
        dto.setRelateInfos(infoDTOs);
        RelateGroup rg2 = repository.findById(1L).get();
        assertThat(rg2.getRelateInfos()).hasSizeGreaterThan(2);

        entityManager.flush();
        entityManager.clear();
        service.editById(dto, 1L);

        entityManager.flush();
        entityManager.clear();

        RelateGroup rg = repository.findById(1L).get();

        assertThat(rg.getName()).isEqualTo("NEW NAME");
        assertThat(rg.getRelateInfos()).hasSize(2).allMatch(ri -> ri.getName().equals("New Info"));
        assertThat(productCount).isEqualTo(productRepository.count());
    }

    @Test
    void testDeleteById() {
        long productCount = productRepository.count();
        long relateInfoCount = relateInfoRepository.count();
        long removeId = 1L;
        RelateGroup rg = repository.findById(1L).get();
        int relateInfoToBeRemoveCount = rg.getRelateInfos().size();

        entityManager.flush();
        entityManager.clear();

        assertThat(repository.existsById(removeId)).isTrue();
        entityManager.flush();
        entityManager.clear();

        System.out.println("BEGIN REMOVE");

        service.deleteById(removeId);
        entityManager.flush();
        entityManager.clear();

        assertThat(repository.existsById(removeId)).isFalse();
        assertThat(relateInfoRepository.count()).isEqualTo(relateInfoCount - relateInfoToBeRemoveCount);
        assertThat(productCount).isEqualTo(productRepository.count());

    }

}
