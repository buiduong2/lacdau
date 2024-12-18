package com.backend.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.dto.res.CategoryAdminDTO;
import com.backend.dto.res.ProductAdminDTO;
import com.backend.utils.ImageUploads;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("rawtypes")
public class ImageCloudService {

    private final ThreadPoolTaskExecutor executor;
    private final Cloudinary cloudinary;

    private final String PUBLIC_ID = "public_id";
    private final String URL = "url";

    /**
     * 
     * 
     * @return [publicId,url] || Null if error
     * 
     */
    public CompletableFuture<String[]> uploadProductMainImage(MultipartFile file, ProductAdminDTO dto) {

        try {
            byte[] bytes = file.getBytes();

            return executor.submitCompletable(uploadMainImageProduct(bytes, dto, file.getOriginalFilename()))
                    .thenApply(this::handleUploadSuccessFull)
                    .exceptionally(this::handleUploadFailed);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 
     * 
     * @return [publicId[] | null, url[] | null]
     * 
     */
    public List<CompletableFuture<String[]>> uploadProductThumnails(List<MultipartFile> files, ProductAdminDTO dto) {

        List<CompletableFuture<String[]>> cfs = new LinkedList<>();
        try {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                byte[] bytes = file.getBytes();
                CompletableFuture<String[]> cf = executor
                        .submitCompletable(uploadThumbnaailsProduct(bytes, dto, file.getOriginalFilename()))
                        .thenApply(this::handleUploadSuccessFull)
                        .exceptionally(this::handleUploadFailed);
                cfs.add(cf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cfs;
    }

    public CompletableFuture<String[]> uploadCategoryImage(MultipartFile file, CategoryAdminDTO dto) {

        try {
            byte[] bytes = file.getBytes();
            return executor.submitCompletable(uploadImagecategory(bytes, dto, file.getOriginalFilename()))
                    .thenApply(this::handleUploadSuccessFull)
                    .exceptionally(this::handleUploadFailed);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(null);
    }

    private String[] handleUploadSuccessFull(Map map) {
        return new String[] { (String) map.get(PUBLIC_ID), (String) map.get(URL) };
    }

    private String[] handleUploadFailed(Throwable ex) {
        ex.printStackTrace();
        return null;
    }

    private Callable<Map> uploadMainImageProduct(byte[] file, ProductAdminDTO dto, String fileName) {
        return upload(file,
                ImageUploads.toMainImageProductName(dto.getNatureId(), fileName),
                ImageUploads.toMainImageProductFolder(dto.getNatureId()));
    }

    private Callable<Map> uploadThumbnaailsProduct(byte[] file, ProductAdminDTO dto, String fileName) {
        return upload(file,
                ImageUploads.toThumbnailsProductName(dto.getNatureId(), fileName),
                ImageUploads.toThumbnailsProductFolder(dto.getNatureId()));
    }

    private Callable<Map> uploadImagecategory(byte[] file, CategoryAdminDTO dto, String fileName) {
        return upload(file,
                ImageUploads.toImageCategoryName(dto.getId(), fileName),
                ImageUploads.toCategoryFolder(dto.getId()));
    }

    private Callable<Map> upload(byte[] file, String publicId, String folder) {
        return () -> {
            Map params1 = ObjectUtils.asMap(
                    "use_filename", true,
                    "public_id", publicId,
                    "asset_folder", folder,
                    "overwrite", true);
            return cloudinary.uploader().upload(file, params1);
        };
    }

    public void deleteCategoryFolder(Long id) {
        if (id != null) {
            executor.submit(() -> {
                deleteFolder(ImageUploads.getCategoryResourcePrefixById(id), ImageUploads.toCategoryFolder(id));
            });
        }
    }

    public void deleteCategoryFolderAll(List<Long> ids) {
        if (ids == null) {
            return;
        }
        executor.submit(() -> {
            for (Long id : ids) {
                deleteFolder(ImageUploads.getCategoryResourcePrefixById(id), ImageUploads.toCategoryFolder(id));
            }
        });
    }

    public void deleteProductFolder(Long id) {
        if (id != null) {
            executor.submit(() -> {
                deleteFolder(ImageUploads.getProductResourcePrefixById(id), ImageUploads.toProductFolder(id));
            });
        }
    }

    public void deleteProductFolderAll(List<Long> deletedIds) {
        if (deletedIds == null) {
            return;
        }
        executor.submit(() -> {
            for (Long id : deletedIds) {
                deleteFolder(ImageUploads.getProductResourcePrefixById(id), ImageUploads.toProductFolder(id));
            }
        });
    }

    private void deleteFolder(String prefix, String folder) {
        try {
            cloudinary.api().deleteResourcesByPrefix(prefix, ObjectUtils.emptyMap());
            cloudinary.api().deleteFolder(folder, ObjectUtils.emptyMap());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAll(List<String> publicIds) {
        for (String publicId : publicIds) {
            delete(publicId);
        }
    }

    public void delete(String publicId) {
        executor.submit(() -> cloudinary.uploader()
                .destroy(publicId,
                        ObjectUtils.emptyMap()));

    }

}
