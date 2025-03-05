package com.backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.req.FilterParam;
import com.backend.dto.res.FilterDTO;
import com.backend.dto.res.ProductInfoDTO;
import com.backend.dto.res.ProductSummaryDTO;
import com.backend.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Page<ProductSummaryDTO> findAll(FilterParam param, Pageable pageable) {
        return productService.findSummariesBy(param, pageable);
    }

    @GetMapping("/list")
    public List<ProductSummaryDTO> findByIdIn(@RequestParam List<String> ids) {
        return productService.finSummaryDTOsByProductCodeIn(ids);
    }

    @GetMapping("/filter/{categoryId}")
    public FilterDTO findFilterByCategoryId(@PathVariable Long categoryId) {
        return productService.findFilterByCategoryId(categoryId);
    }

    @ResponseBody
    @GetMapping("{id}")
    public ProductInfoDTO findDetailByProductCode(@PathVariable String id) {
        return productService.findDetailByProductCode(id);
    }

}
