package com.backend.service;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.backend.dto.req.CategoryAdminUpdateDTO;
import com.backend.dto.res.CategoryAdminDTO;
import com.backend.dto.res.CategoryDTO;
import com.backend.entities.Category;
import com.backend.exception.ResourceNotFoundException;
import com.backend.exception.ValidationException;
import com.backend.mapper.CategoryMapper;
import com.backend.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class CategoryService {

    private final CategoryRepository repository;

    private final CategoryMapper mapper;

    public List<CategoryDTO> findAllDtos() {
        return repository.findAllDtosBy();
    }

    public List<CategoryAdminDTO> findAllAdminDtos() {
        return repository.findAllAdminDtosBy();
    }

    public Category findById(long id) {
        return repository.findById(id)
                .orElseThrow(notFoundException(id));
    }

    public CategoryAdminDTO findDTOById(long id) {
        return repository.findDTOById(id).orElseThrow(notFoundException(id));
    }

    private Supplier<ResourceNotFoundException> notFoundException(long id) {
        return () -> new ResourceNotFoundException("Category with id " + id + " is not found");
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public CategoryAdminDTO create(CategoryAdminUpdateDTO dto) {
        Category category = mapper.toEntity(dto);
        Long parentId = dto.getParentId();
        if (parentId != null) {
            Category parent = findById(dto.getParentId());
            category.setParent(parent);
        }
        repository.saveAndFlush(category);
        validateCategory(category.getId());
        return mapper.toAdminDTO(category);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public CategoryAdminDTO editById(Long id, CategoryAdminUpdateDTO dto) {
        if (id == dto.getParentId()) {
            throw new ValidationException("parentId", "CategoryId And parent Id must not be the same");
        }
        Category category = findById(id);
        mapper.updateEntity(category, dto);

        if (dto.getParentId() == null) {
            category.setParent(null);
        } else {
            category.setParent(findById(dto.getParentId()));
        }
        repository.saveAndFlush(category);
        validateCategory(category.getId());
        return mapper.toAdminDTO(category);
    }

    public void validateCategory(long categoryId) {
        long getDepthToRoot = repository.getDepthToRoot(categoryId);
        boolean isValid = true;
        if (getDepthToRoot > 2) {
            isValid = false;
        }

        long getDepthToLeaf = repository.getDepthToLeaf(categoryId);

        if (getDepthToLeaf > 2) {
            isValid = false;
        }

        if (getDepthToLeaf + getDepthToLeaf > 2) {
            isValid = false;
        }

        if (!isValid) {
            throw new ValidationException("parentId", "Parent id is not valid. It make tree circle or too deep");
        }

    }

    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteByIdIn(List<Long> ids) {
        repository.deleteAllById(ids);
    }

}
