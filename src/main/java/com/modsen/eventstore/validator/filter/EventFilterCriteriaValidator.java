package com.modsen.eventstore.validator.filter;

import com.modsen.eventstore.dto.criteria.event.EventCriteriaField;
import com.modsen.eventstore.dto.criteria.event.EventFilterCriteria;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

public class EventFilterCriteriaValidator implements ConstraintValidator<EventFilterList, List<EventFilterCriteria>> {

    private String PLANNER_REGEXP;
    private String SUBJECT_REGEXP;
    private String DATE_REGEXP;
    private String TIME_REGEXP;

    @Override
    public void initialize(EventFilterList constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        PLANNER_REGEXP = constraintAnnotation.plannerRegexp();
        SUBJECT_REGEXP = constraintAnnotation.subjectRegexp();
        DATE_REGEXP = constraintAnnotation.dateRegexp();
        TIME_REGEXP = constraintAnnotation.timeRegexp();
    }

    @Override
    public boolean isValid(List<EventFilterCriteria> criteriaList, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(criteriaList)) {
            return true;
        }

        for (EventFilterCriteria criteria : criteriaList) {
            if (!isValidCriteria(criteria)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidCriteria(EventFilterCriteria criteria) {
        if (Objects.isNull(criteria.getField()) || Objects.isNull(criteria.getValue())) {
            return false;
        }

        if (criteria.getField() == EventCriteriaField.SUBJECT) {
            return criteria.getValue().matches(SUBJECT_REGEXP);
        }

        if (criteria.getField() == EventCriteriaField.PLANNER) {
            return criteria.getValue().matches(PLANNER_REGEXP);
        }

        if (criteria.getField() == EventCriteriaField.DATE) {
            return criteria.getValue().matches(DATE_REGEXP);
        }

        if (criteria.getField() == EventCriteriaField.TIME) {
            return criteria.getValue().matches(TIME_REGEXP);
        }

        return true;
    }
}