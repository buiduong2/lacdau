package com.backend.service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.backend.dto.req.ImageAdminUpdateDTO;
import com.backend.dto.req.ImageAdminUpdateDTO.ImageCreateDTO;
import com.backend.dto.req.ImageAdminUpdateDTO.ImageEditDTO;
import com.backend.entities.Category;
import com.backend.entities.Image;
import com.backend.entities.Product;
import com.backend.exception.ResourceNotFoundException;
import com.backend.repository.CategoryRepository;
import com.backend.repository.ImageRepository;
import com.backend.repository.ProductDetailRepository;
import com.backend.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageStorageService {

    private final ImageRepository repository;

    private final ProductRepository productRepository;

    private final ProductDetailRepository detailRepository;

    private final CategoryRepository categoryRepository;

    @Transactional
    public String saveProductMainImage(String publicId, String url, Long productId) {
        Image image = getMainImageByProductId(productId);
        String deprecatePublicId = image.getPublicId();
        if (publicId != null && url != null) {
            image.setAlt("ALt");
            image.setPublicId(publicId);
            image.setSrc(url);
        } else {
            image.setAlt("ALT");
            image.setSrc(null);
        }
        repository.saveAndFlush(image);
        return deprecatePublicId;
    }

    private Image getMainImageByProductId(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id = " + productId + " not found"));

        Image image = product.getMainImage();
        if (image == null) {
            image = new Image();
            product.setMainImage(image);
        }

        return image;
    }

    @Transactional
    public void saveProductThumnails(List<String[]> publicIdUrls, long detailId,
            List<ImageCreateDTO> imageDTOs) {
        List<Image> images = new LinkedList<>();
        for (int i = 0; i < imageDTOs.size(); i++) {
            ImageCreateDTO dto = imageDTOs.get(i);
            String[] publicIdUrl = publicIdUrls.get(i);
            String publicId = null;
            String src = null;
            if (publicIdUrl != null) {
                publicId = publicIdUrls.get(i)[0];
                src = publicIdUrls.get(i)[1];
            }

            Image image = new Image();
            image.setAlt("ALT");
            image.setPosition(dto.getPosition());
            image.setProductDetail(detailRepository.getReferenceById(detailId));
            image.setPublicId(publicId);
            image.setSrc(src);

            images.add(image);
        }

        repository.saveAll(images);
    }

    @Transactional
    public String saveCategoryImage(String publicId, String url, long categoryId) {
        Image image = this.getImageByCategoryId(categoryId);
        String deletePublicId = image.getPublicId();
        
        image.setAlt("ALT");
        image.setSrc(url);
        image.setPublicId(publicId);
        repository.save(image);
        return deletePublicId;
    }

    private Image getImageByCategoryId(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category + with id = " + categoryId + " is not found"));
        Image image = category.getImage();
        if (image == null) {
            image = new Image();
            category.setImage(image);
        }
        return image;
    }

    /**
     * @return removedImages
     */
    @Transactional
    public List<String> editThumbnails(long detailId, ImageAdminUpdateDTO imageDTO) {

        List<Image> images = repository.findByProductDetailId(detailId);
        Map<Long, Image> mapById = images.stream().collect(Collectors.toMap(Image::getId, Function.identity()));

        for (ImageEditDTO dto : imageDTO.getEditThumbnails()) {
            Long id = dto.getId();
            if (mapById.containsKey(id)) {
                Image image = mapById.get(id);
                image.setPosition(dto.getPosition());
                mapById.remove(id);
            }
        }

        Collection<Image> missingImages = mapById.values();
        repository.deleteAllInBatch(missingImages);
        List<String> deprecatePublicIds = missingImages.stream().map(Image::getPublicId).toList();

        return deprecatePublicIds;
    }

}
