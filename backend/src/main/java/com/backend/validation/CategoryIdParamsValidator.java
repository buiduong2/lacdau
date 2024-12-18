package com.backend.validation;

import com.backend.dto.req.CategoryAdminUpdateDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class CategoryIdParamsValidator implements ConstraintValidator<CategoryIdParams, Object[]> {

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        int indexId = 0;
        int indexDto = 1;
        Long categoryId = null;
        Long parentId = null;
        if (value[indexId] instanceof Long) {
            categoryId = (Long) value[indexId];
        } else {
            throw new IllegalArgumentException("Index = 0 must be a pathvairable categoryId");
        }

        if (value[indexDto] instanceof CategoryAdminUpdateDTO) {
            parentId = ((CategoryAdminUpdateDTO) value[indexDto]).getParentId();
        } else {
            throw new IllegalArgumentException("Param index = 1 must be intance of CategoryAdminUpdateDTO");
        }

        if (parentId != null && categoryId == parentId) {
            return false;
        }

        return true;
    }

}
