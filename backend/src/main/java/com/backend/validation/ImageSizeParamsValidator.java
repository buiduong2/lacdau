package com.backend.validation;

import java.lang.reflect.Field;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class ImageSizeParamsValidator implements ConstraintValidator<ImageSizeParams, Object[]> {

    private int indexImages;
    private int indexMetadatas;
    private String path;
    private String imageParamName;
    private String metadataParamName;

    @Override
    public void initialize(ImageSizeParams constraintAnnotation) {
        this.indexImages = constraintAnnotation.indexImages();
        this.indexMetadatas = constraintAnnotation.indexMetadatas();
        this.path = constraintAnnotation.pathToMetadata();
        this.imageParamName = constraintAnnotation.imageParamName();
        this.metadataParamName = constraintAnnotation.metadataParamName();
    }

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        try {
            context.disableDefaultConstraintViolation();
            if (indexImages >= value.length || indexMetadatas >= value.length) {
                throw new RuntimeException(
                        String.format("Array out of bound %s,%s", imageParamName, metadataParamName));
            }
            List<?> metadatas = null;
            if (value[indexMetadatas] != null) {
                metadatas = (List<?>) getMetadataList(value);
            }

            if (value[indexImages] == null && metadatas == null) {
                return true;
            }

            if (metadatas == null) {
                context.buildConstraintViolationWithTemplate(
                        String.format("%s not null but %s is null", imageParamName,
                                metadataParamName))
                        .addPropertyNode(imageParamName)
                        .addConstraintViolation();
                return false;
            }

            if (value[indexImages] == null) {
                context.buildConstraintViolationWithTemplate(
                        String.format("%s null but %s is not  null", imageParamName,
                                metadataParamName))
                        .addPropertyNode(imageParamName)
                        .addConstraintViolation();
                return false;
            }

            if (!(value[indexImages] instanceof List<?>)) {
                throw new RuntimeException("index Images must be instanceof List<MultipartFile>");
            }

            List<?> files = (List<?>) value[indexImages];
            if (files.size() != metadatas.size()) {
                context.buildConstraintViolationWithTemplate(
                        String.format("Size of %s %s are not matches", metadataParamName, imageParamName))
                        .addPropertyNode(imageParamName)
                        .addConstraintViolation();

                return false;
            }

            return true;

        } catch (NoSuchFieldException | SecurityException e) {
            System.err.println("path with indexOfMetadata Not matches");
            e.printStackTrace();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            System.err.println("Access Field faile");
            e.printStackTrace();
        }
        throw new RuntimeException("Some thing went wrong");
    }

    private List<?> getMetadataList(Object[] value)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Object obj = value[indexMetadatas];
        Class<?> type = obj.getClass();
        Field field = type.getDeclaredField(path);
        field.setAccessible(true);
        Object fieldValue = field.get(obj);
        field.setAccessible(false);

        if (fieldValue == null) {
            return null;
        }

        if (!(fieldValue instanceof List)) {
            throw new RuntimeException("Metadata should be a java.util.List");
        }
        return (List<?>) fieldValue;
    }

}
