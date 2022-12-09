package com.modsen.eventstore.validator.sorting;

import com.modsen.eventstore.dto.criteria.event.EventSortingCriteria;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

public class EventSortingCriteriaValidator implements ConstraintValidator<EventSortingList, List<EventSortingCriteria>> {

    @Override
    public void initialize(EventSortingList constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<EventSortingCriteria> criteriaList, ConstraintValidatorContext context) {
        if (Objects.isNull(criteriaList)) {
            return true;
        }

        for (EventSortingCriteria criteria : criteriaList) {
            if (Objects.isNull(criteria.getField()) || Objects.isNull(criteria.getDirection())) {
                return false;
            }
        }

        return true;
    }
}
