package com.backend.mapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.backend.dto.req.RelateGroupAdminUpdateDTO;
import com.backend.dto.req.RelateGroupAdminUpdateDTO.RelateInfoDTO;
import com.backend.entities.Product;
import com.backend.entities.RelateGroup;
import com.backend.entities.RelateInfo;
import com.backend.exception.ResourceNotFoundException;
import com.backend.repository.ProductRepository;
import com.backend.repository.RelateInfoRepository;

public abstract class RelateGroupMapperDecorator extends RelateGroupMapper {

    @Autowired
    @Qualifier("delegate")
    private RelateGroupMapper delegate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RelateInfoRepository relateInfoRepository;

    public RelateGroup toEntity(RelateGroupAdminUpdateDTO dto) {
        RelateGroup rg = delegate.toEntity(dto);
        mappingRelateInfo(rg, dto.getRelateInfos());
        return rg;
    }

    @Override
    public void updateEntity(RelateGroup entity, RelateGroupAdminUpdateDTO dto) {
        delegate.updateEntity(entity, dto);
        mappingRelateInfo(entity, dto.getRelateInfos());
        removeRelateInfo(entity, dto.getRelateInfos());
    }

    private void mappingRelateInfo(RelateGroup rg, List<RelateInfoDTO> relateInfoDTOs) {
        if (relateInfoDTOs != null && !relateInfoDTOs.isEmpty()) {
            List<String> codes = relateInfoDTOs.stream().map(RelateInfoDTO::getProductId).toList();

            Map<String, Product> mapProductByCode = productRepository.findWithRelateByProductCodeIn(codes)
                    .stream()
                    .collect(Collectors.toMap(Product::getProductCode, Function.identity()));

            for (RelateInfoDTO riDTO : relateInfoDTOs) {
                String code = riDTO.getProductId();
                if (mapProductByCode.containsKey(code)) {
                    Product product = mapProductByCode.get(code);
                    RelateInfo relateInfo = product.getRelateInfo();
                    if (relateInfo == null) {
                        relateInfo = new RelateInfo();
                        product.setRelateInfo(relateInfo);
                    }
                    updateRelateInfo(relateInfo, riDTO);
                    rg.addRelateInfo(relateInfo);
                } else {
                    throw new ResourceNotFoundException("Product with id = " + code + " not FOund");
                }

            }
        }
    }

    private void removeRelateInfo(RelateGroup rg, List<RelateInfoDTO> relateInfoDTOs) {
        Long groupId = rg.getId();
        if (relateInfoDTOs != null && !relateInfoDTOs.isEmpty()) {
            List<String> codes = relateInfoDTOs.stream().map(RelateInfoDTO::getProductId).toList();
            relateInfoRepository.deleteByProductCodeNotInAndGroupId(codes, groupId);
        } else {
            relateInfoRepository.deleteByRelateGroupId(groupId);
        }
    }

}
