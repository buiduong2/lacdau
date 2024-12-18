package com.backend.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.backend.dto.req.FilterParam;
import com.backend.dto.req.FilterProductAdmin;
import com.backend.dto.req.ProductAdminFullDTO;
import com.backend.dto.req.ProductUpdateFullDTO;
import com.backend.dto.res.FilterDTO;
import com.backend.dto.res.FilterDTO.FilterGroup;
import com.backend.dto.res.FilterDTO.FilterItemDTO;
import com.backend.dto.res.PriceInfo;
import com.backend.dto.res.ProductAdminDTO;
import com.backend.dto.res.ProductInfoDTO;
import com.backend.dto.res.ProductRelateInfoAdminDTO;
import com.backend.dto.res.ProductSummaryDTO;
import com.backend.entities.Attribute;
import com.backend.entities.AttributeValue;
import com.backend.entities.Brand;
import com.backend.entities.Product;
import com.backend.entities.ProductStatus;
import com.backend.exception.ResourceNotFoundException;
import com.backend.mapper.ProductMapper;
import com.backend.repository.AttributeRepository;
import com.backend.repository.AttributeValueRepository;
import com.backend.repository.BrandRepository;
import com.backend.repository.CategoryRepository;
import com.backend.repository.ProductRepository;
import com.backend.repository.RelateGroupRepository;
import com.backend.utils.ProductSpecs;
import com.backend.utils.Utils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final RelateGroupRepository relateGroupRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final AttributeRepository attributeRepository;
    private final ProductCodeGeneratorService productCodeGeneratorService;

    private final ProductMapper productMapper;
    private final ProductSpecs productSpecs;

    public ProductInfoDTO findDetailByProductCode(String id) {
        return productRepository
                .findDetailByProductCodeAndStatus(id, ProductStatus.ACTIVE)
                .map(productMapper::toProductInfoDto)
                .orElseThrow(getNotFoundException(id));
    }

    public Product findById(String id) {
        return productRepository.findByProductCode(id)
                .orElseThrow(getNotFoundException(id));
    }

    public ProductAdminFullDTO findForUpdateFullById(String id) {
        return productRepository.findForUpdateFullByProductCode(id)
                .map(productMapper::toUpdateFullDTO)
                .orElseThrow(getNotFoundException(id));
    }

    public ProductRelateInfoAdminDTO findAdminRelateById(String id) {
        return productRepository.findWithRelateByProductCode(id)
                .map(productMapper::toProductRelateInfoAdminDTO)
                .orElseThrow(getNotFoundException(id));
    }

    public List<ProductRelateInfoAdminDTO> findAdminRelateByGroupId(long id) {
        return productRepository
                .findWithRelateByGroupId(id)
                .stream()
                .map(productMapper::toProductRelateInfoAdminDTO)
                .toList();
    }

    private Supplier<ResourceNotFoundException> getNotFoundException(String id) {
        return () -> new ResourceNotFoundException("Product with id " + id + " not found");
    }

    public Page<ProductSummaryDTO> findSummariesBy(FilterParam param, Pageable pageable) {

        List<Order> customSorts = pageable.getSort().filter(s -> s.getProperty().equals("price")).toList();

        pageable = customSorts.isEmpty() ? pageable : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        return productRepository.findAll(productSpecs.getSpecs(param, customSorts), pageable)
                .map(productMapper::toProductSummaryDto);
    }

    public Page<ProductAdminDTO> findAdminDtoBy(FilterProductAdmin param, Pageable pageable) {
        return productRepository.findAll(productSpecs.byFilter(param), pageable)
                .map(productMapper::toProductAdminDto);
    }

    public FilterDTO findFilterByCategoryId(Long id) {
        FilterDTO filterDto = new FilterDTO();
        List<Long> cateogryIds = categoryRepository.findIdsWithIdbyParentId(id);
        List<Brand> brands = productRepository.findAllBrandsByCategoryIdInAndProductStatusIsActive(cateogryIds);
        PriceInfo minMaxPrice = productRepository.findMinMaxPriceByCategoryIdInAndStatusIsActive(cateogryIds);

        List<Attribute> productAttributes = attributeRepository.findByProductCategoryInAndStatusIsActive(cateogryIds);

        FilterGroup[] filterGroups = productMapper.toFilterAttributeDto(productAttributes);

        filterDto.setBrand(
                new FilterGroup(
                        "HÃNG SẢN XUẤT",
                        brands.stream().map(productMapper::toFilterItemDto).toArray(FilterItemDTO[]::new)));
        filterDto.setPrice(minMaxPrice);
        filterDto.setAttributes(filterGroups);

        return filterDto;
    }

    @Transactional
    public ProductAdminDTO editById(String id, ProductUpdateFullDTO productDTO) {
        Product product = this.findById(id);
        this.productMapper.updateFullEntity(product, productDTO);
        this.setAssociation(product, productDTO);
        return productMapper.toProductAdminDto(product);
    }

    @Transactional
    public ProductAdminDTO create(ProductUpdateFullDTO dto) {
        Product product = new Product();
        this.productMapper.updateFullEntity(product, dto);
        this.setAssociation(product, dto);
        productRepository.save(product);
        return productMapper.toProductAdminDto(product);
    }

    private void setAssociation(Product product, ProductUpdateFullDTO productDTO) {
        Long brandId = productDTO.getBrandId();
        Long categoryId = productDTO.getCategoryId();
        List<Long> attributeValueIds = productDTO.getAttributeValueIds();
        String prefix = productDTO.getPrefixProductCode();

        if (!Utils.isPrefixOfProductCode(product.getProductCode(), prefix)) {
            String productCode = productCodeGeneratorService.getNextCounterByKey(prefix);
            product.setProductCode(productCode);
        }

        if (brandId == null) {
            product.setBrand(null);
        } else {
            product.setBrand(brandRepository.getReferenceById(brandId));
        }

        if (categoryId == null) {
            product.setCategory(null);
        } else {
            product.setCategory(categoryRepository.getReferenceById(categoryId));
        }

        if (productDTO.getRelateInfo() != null && productDTO.getRelateInfo().getRelateGroupId() != null) {
            Long relateGroupId = productDTO.getRelateInfo().getRelateGroupId();
            product.getRelateInfo().setRelateGroup(relateGroupRepository.getReferenceById(relateGroupId));
        }

        if (attributeValueIds != null && !attributeValueIds.isEmpty()) {
            Set<AttributeValue> attributeValues = attributeValueIds
                    .stream()
                    .distinct()
                    .map(attributeValueRepository::getReferenceById)
                    .collect(Collectors.toSet());

            if (product.getAttributeValues() != null) {
                product.getAttributeValues().retainAll(attributeValues);
                product.getAttributeValues().addAll(attributeValues);
            } else {
                product.setAttributeValues(attributeValues);
            }

        } else {
            product.setAttributeValues(null);
        }
    }

    @Transactional
    public Long deleteByProductCode(String code) {
        Long id = productRepository.getIdByProductCode(code);
        int count = productRepository.deleteByProductCode(code);
        if (count == 0) {
            throw new ResourceNotFoundException("Product with Id: " + code + " does not exists");
        } else if (count != 1) {
            throw new RuntimeException("Bug in productRepository.deleteByProductCode " + code);
        }
        return id;
    }

    @Transactional
    public List<Long> deleteByProductCodeIn(String[] codes) {
        List<Long> ids = productRepository.getIdsByProductCodeIn(codes);
        int count = productRepository.deleteByProductCodeIn(codes);
        if (count > codes.length) {
            throw new RuntimeException("Bug in productRepository.deleteByProductCode " + Arrays.toString(codes));
        }
        return ids;

    }

}
