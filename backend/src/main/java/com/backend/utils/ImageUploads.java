package com.backend.utils;

public class ImageUploads {

    private static String productPublicIdPrefix = "product/";
    private static String categoryPublicIdPrefix = "category/";

    public static String getProductResourcePrefixById(long productId) {
        return productPublicIdPrefix + productId;
    }

    public static String getCategoryResourcePrefixById(long categoryId) {
        return categoryPublicIdPrefix + categoryId;
    }

    public static String toCategoryFolder(long categoryId) {
        return "/categoies/";
    }

    public static String toProductFolder(long productId) {
        return "/products/" + productId;
    }

    public static String toMainImageProductFolder(long productId) {
        return toProductFolder(productId) + "/main";
    }

    public static String toThumbnailsProductFolder(long productId) {
        return toProductFolder(productId) + "/thumbnails";
    }

    public static String toThumbnailsProductName(long productId, String fileName) {
        return getProductResourcePrefixById(productId) + "_" + System.currentTimeMillis();
    }

    public static String toMainImageProductName(long productId, String fileName) {
        return getProductResourcePrefixById(productId);
    }

    public static String toImageCategoryName(long categoryId, String fileNmae) {
        return getCategoryResourcePrefixById(categoryId);
    }
}
