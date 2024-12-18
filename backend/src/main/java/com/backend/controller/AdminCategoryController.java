package com.backend.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.dto.req.CategoryAdminUpdateDTO;
import com.backend.dto.res.CategoryAdminDTO;
import com.backend.service.CategoryService;
import com.backend.service.ImageManagerService;
import com.backend.validation.CategoryIdParams;
import com.backend.validation.Image;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Validated
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final ImageManagerService imageSerivce;

    @GetMapping
    public List<CategoryAdminDTO> findAllAdminDtos() {
        return categoryService.findAllAdminDtos();
    }

    @GetMapping("{id}")
    public CategoryAdminDTO findById(@PathVariable long id) {
        return categoryService.findDTOById(id);
    }

    @PostMapping
    public CategoryAdminDTO create(
            @RequestPart("category") @Valid CategoryAdminUpdateDTO dto,
            @RequestParam(required = false) @Image MultipartFile image) {

        CategoryAdminDTO res = categoryService.create(dto);
        imageSerivce.saveCategoryImage(image, res);

        return res;
    }

    @CategoryIdParams
    @PutMapping("{id}")
    public CategoryAdminDTO edit(
            @PathVariable Long id, @RequestPart("category") @Valid CategoryAdminUpdateDTO dto,
            @RequestParam(required = false) @Image MultipartFile image) {
        CategoryAdminDTO res = categoryService.editById(id, dto);
        imageSerivce.saveCategoryImage(image, res);
        return res;
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable long id) {
        categoryService.deleteById(id);
    }

    @DeleteMapping
    public void deleteByIdIn(@RequestParam List<Long> id) {
        categoryService.deleteByIdIn(id);
    }

}
