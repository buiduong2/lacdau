package com.backend.service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.dto.req.ImageAdminUpdateDTO;
import com.backend.dto.res.CategoryAdminDTO;
import com.backend.dto.res.ProductAdminDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageManagerService {

    private final ImageCloudService cloudService;
    private final ImageStorageService storageService;

    public void saveProductMainImage(MultipartFile mainImage, ProductAdminDTO product) {
        if (mainImage != null) {
            CompletableFuture<String[]> cf = cloudService.uploadProductMainImage(mainImage,
                    product);

            cf.thenApply(publicIdAndUrls -> {
                if (publicIdAndUrls == null) {
                    return storageService.saveProductMainImage(null, null, product.getNatureId());
                } else {
                    return storageService.saveProductMainImage(publicIdAndUrls[0], publicIdAndUrls[1],
                            product.getNatureId());
                }
            }).thenAccept(deletePublicId -> {
                if (deletePublicId != null) {
                    this.cloudService.delete(deletePublicId);
                }
            });
        }

    }

    public void saveProducThumbnails(List<MultipartFile> thumbnails, ProductAdminDTO res,
            ImageAdminUpdateDTO imageDTO) {

        if (thumbnails != null) {
            List<CompletableFuture<String[]>> cfs = cloudService.uploadProductThumnails(thumbnails,
                    res);

            CompletableFuture.allOf(cfs.toArray(new CompletableFuture[cfs.size()]))
                    .whenComplete((v, ex) -> {
                        List<String[]> publicIdUrls = new LinkedList<>();
                        for (CompletableFuture<String[]> cf : cfs) {
                            publicIdUrls.add(cf.join());
                        }

                        storageService.saveProductThumnails(publicIdUrls, res.getDetailId(),
                                imageDTO.getCreateThumbnails());
                    });

        }

    }

    public void editProductThumbnails(List<MultipartFile> thumbnails, ProductAdminDTO res,
            ImageAdminUpdateDTO imageDTO) {
        if (imageDTO == null) {
            return;
        }
        if (imageDTO.getEditThumbnails() != null && res.getDetailId() != null) {
            List<String> deprecatePublicIds = this.storageService.editThumbnails(res.getDetailId(), imageDTO);
            this.cloudService.deleteAll(deprecatePublicIds);
        }

        this.saveProducThumbnails(thumbnails, res, imageDTO);
    }

    public void saveCategoryImage(MultipartFile image, CategoryAdminDTO dto) {
        if (image != null) {
            this.cloudService.uploadCategoryImage(image, dto)
                    .thenApply(publicIdAndUrl -> {
                        System.out.println(Arrays.toString(publicIdAndUrl));
                        if (publicIdAndUrl == null) {
                            return this.storageService.saveCategoryImage(null, null, dto.getId());
                        } else {
                            return this.storageService.saveCategoryImage(publicIdAndUrl[0], publicIdAndUrl[1],
                                    dto.getId());
                        }
                    }).thenAccept(deletePublicId -> {
                        if (deletePublicId != null) {
                            this.cloudService.delete(deletePublicId);
                        }
                    });
        }
    }

    public void removeProductFolder(Long deletedId) {
       this.cloudService.deleteProductFolder(deletedId);
    }

    public void removeProductFolderIn(List<Long> deletedIds) {
        this.cloudService.deleteProductFolderAll(deletedIds);
    }

}
