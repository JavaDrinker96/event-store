package com.modsen.eventstore.validator.pagination;

import com.modsen.eventstore.dto.criteria.PaginationCriteria;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class PaginationValidator implements ConstraintValidator<Pagination, PaginationCriteria> {
    @Override
    public void initialize(Pagination constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PaginationCriteria pagination, ConstraintValidatorContext context) {
        if (Objects.isNull(pagination)) {
            return true;
        }

        if (Objects.isNull(pagination.getPage()) || Objects.isNull(pagination.getSize())) {
            return false;
        }

        return pagination.getPage() >= 1 && pagination.getSize() >= 1;
    }
}
