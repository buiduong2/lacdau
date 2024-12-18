package com.backend.utils;

import org.springframework.web.multipart.MultipartFile;

public class Utils {

    public static boolean isPrefixOfProductCode(String productCode, String prefix) {
        if (productCode == null || prefix == null) {
            return false;
        }
        int index = productCode.indexOf(prefix);
        boolean isContainPrefix = index != -1 && index + prefix.length() < productCode.length() - 1;
        index = prefix.length() - 1;
        if (isContainPrefix) {
            boolean isContainCounter = productCode.charAt(index + 1) >= '0' && productCode.charAt(index + 1) <= '9';
            return isContainCounter;
        }

        return false;
    }

    public static String getFormatProductCode(String prefix, long counter) {
        return String.format("%s%05d", prefix, counter);
    }

    public static boolean isImageFileValid(MultipartFile file) {

        
        return true;
    }
}
