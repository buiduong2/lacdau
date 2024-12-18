package com.backend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.backend.mapper.AttributeMapper;
import com.backend.mapper.AttributeMapperImpl;
import com.backend.mapper.AttributeMapperImpl_;
import com.backend.mapper.AttributeValueMapperImpl;
import com.backend.mapper.BrandMapperImpl;
import com.backend.mapper.CategoryMapper;
import com.backend.mapper.ProductMapperImpl;
import com.backend.mapper.RelateGroupMapper;
import com.backend.mapper.RelateGroupMapperImpl;
import com.backend.mapper.RelateGroupMapperImpl_;
import com.backend.repository.AttributeRepository;
import com.backend.repository.AttributeValueRepository;
import com.backend.repository.BrandRepository;
import com.backend.repository.CategoryRepository;
import com.backend.repository.ProductCodeGeneratorRepository;
import com.backend.repository.ProductRepository;
import com.backend.repository.RelateGroupRepository;
import com.backend.repository.RelateInfoRepository;
import com.backend.utils.ProductSpecs;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Configuration
public class TestConfig {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private AttributeValueRepository attributeValueRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private RelateGroupRepository relateGroupRepository;

    @Autowired
    private RelateInfoRepository relateInfoRepository;

    @Autowired
    private ProductCodeGeneratorRepository productCodeGeneratorRepository;

    @Bean
    ProductService productService() {
        return new ProductService(productRepository,
                categoryRepository,
                brandRepository,
                relateGroupRepository,
                attributeValueRepository,
                attributeRepository,
                productCodeGeneratorService(),
                new ProductMapperImpl(),
                productSpecs());
    }

    @Bean
    BrandService brandService() {
        return new BrandService(brandRepository, new BrandMapperImpl());
    }

    @Bean
    ProductCodeGeneratorService productCodeGeneratorService() {
        return new ProductCodeGeneratorService(productCodeGeneratorRepository);
    }

    @Bean
    ProductSpecs productSpecs() {
        return new ProductSpecs(categoryRepository);
    }

    @Bean
    Validator validator() {
        return Validation.byDefaultProvider().configure().buildValidatorFactory().getValidator();
    }

    @Bean
    AttributeService attributeService() {
        return new AttributeService(attributeRepository, attributeMapper());
    }

    @Bean
    AttributeMapper attributeMapper() {
        return new AttributeMapperImpl();
    }

    @Bean
    @Qualifier("delegate")
    AttributeMapper attributeMapperdelegate() {
        return new AttributeMapperImpl_();
    }

    @Bean
    AttributeValueService AttributeValueService() {
        return new AttributeValueService(attributeValueRepository, attributeRepository, new AttributeValueMapperImpl());
    }

    @Bean
    RelateGroupService relateGroupService() {
        return new RelateGroupService(relateGroupRepository, relateGroupMapper(), relateInfoRepository);
    }

    @Bean
    @Primary
    RelateGroupMapper relateGroupMapper() {
        return new RelateGroupMapperImpl();
    }

    @Bean
    @Qualifier("delegate")
    RelateGroupMapper RelateGroupMapperDelegate() {
        return new RelateGroupMapperImpl_();
    }

    @Bean
    CategoryMapper categoryMapper() {
        return Mappers.getMapper(CategoryMapper.class);
    }

    @Bean
    CategoryService categoryService() {
        return new CategoryService(categoryRepository, categoryMapper());
    }
}