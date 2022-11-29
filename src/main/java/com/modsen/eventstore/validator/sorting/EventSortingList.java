package com.modsen.eventstore.validator.sorting;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {EventSortingCriteriaValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface EventSortingList {

    String message() default "The sorting field and the sorting direction cannot be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}