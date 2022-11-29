package com.modsen.eventstore.validator.pagination;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {PaginationValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Pagination {

    String message() default "The page number and page size must be not null and not less than 1.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}