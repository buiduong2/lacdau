package com.backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
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

import com.backend.dto.req.FilterProductAdmin;
import com.backend.dto.req.ImageAdminUpdateDTO;
import com.backend.dto.req.ProductAdminFullDTO;
import com.backend.dto.req.ProductUpdateFullDTO;
import com.backend.dto.res.ProductAdminDTO;
import com.backend.dto.res.ProductRelateInfoAdminDTO;
import com.backend.service.ImageManagerService;
import com.backend.service.ProductService;
import com.backend.validation.Image;
import com.backend.validation.ImageSizeParams;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Validated
public class AdminProductController {

    private final ProductService productService;

    private final ImageManagerService imageService;

    @GetMapping
    public Page<ProductAdminDTO> findAll(FilterProductAdmin filterParam,
            @PageableDefault(size = 10) Pageable pageable) {
        return productService.findAdminDtoBy(filterParam, pageable);
    }

    @GetMapping("/list")
    public List<ProductAdminDTO> findByIdIn(@RequestParam List<String> ids) {
        return productService.findAdminDTOByProductCodeIn(ids);
    }

    @ImageSizeParams(indexImages = 2, indexMetadatas = 3, pathToMetadata = "createThumbnails", imageParamName = "thumbnails", metadataParamName = "createThumbnails")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductAdminDTO create(
            @RequestPart("product") @Valid ProductUpdateFullDTO dto,
            @RequestPart(name = "mainImage", required = false) @Image MultipartFile mainImage,
            @RequestPart(name = "thumbnails", required = false) @Size(min = 1) List<@Image(required = true) MultipartFile> thumbnails,
            @RequestPart(name = "imageData", required = false) @Valid ImageAdminUpdateDTO imageDTO) {

        ProductAdminDTO res = productService.create(dto);
        imageService.saveProductMainImage(mainImage, res);
        imageService.saveProducThumbnails(thumbnails, res, imageDTO);

        return res;
    }

    @GetMapping("{id}")
    public ProductAdminFullDTO findById(@PathVariable String id) {
        return productService.findForUpdateFullById(id);
    }

    @GetMapping("{id}/relate-info")
    public ProductRelateInfoAdminDTO findProductRelateById(@PathVariable String id) {
        return productService.findAdminRelateById(id);
    }

    @GetMapping("relate-info/relate-group/{id}")
    public List<ProductRelateInfoAdminDTO> findProductRelateByGroupId(@PathVariable long id) {
        return productService.findAdminRelateByGroupId(id);
    }

    @ImageSizeParams(indexImages = 2, indexMetadatas = 3, pathToMetadata = "createThumbnails", imageParamName = "thumbnails", metadataParamName = "createThumbnails")
    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductAdminDTO editById(
            @RequestPart("product") ProductUpdateFullDTO productDTO,
            @RequestPart(name = "mainImage", required = false) @Image MultipartFile mainImage,
            @RequestPart(name = "thumbnails", required = false) @Size(min = 1) List<@Image MultipartFile> thumbnails,
            @RequestPart(name = "imageData", required = false) @Valid ImageAdminUpdateDTO imageDTO,
            @PathVariable String id) {

        ProductAdminDTO dto = productService.editById(id, productDTO);
        imageService.saveProductMainImage(mainImage, dto);
        imageService.editProductThumbnails(thumbnails, dto, imageDTO);
        return dto;
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable String id) {
        Long deletedId = productService.deleteByProductCode(id);
        imageService.removeProductFolder(deletedId);
    }

    @DeleteMapping
    public void eleteByIdIn(@RequestParam("id") String[] ids) {
        List<Long> deletedIds = productService.deleteByProductCodeIn(ids);
        imageService.removeProductFolderIn(deletedIds);
    }
}
