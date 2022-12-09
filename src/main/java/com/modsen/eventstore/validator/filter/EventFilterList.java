package com.modsen.eventstore.validator.filter;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {EventFilterCriteriaValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface EventFilterList {

    String message() default "The value of all event filter criteria must match the regular expression of the selected field name";

    String dateRegexp() default "^\\d{2}.\\d{2}.\\d{4}$";

    String timeRegexp() default "^\\d{2}:\\d{2}$";

    String plannerRegexp() default "^[\\s\\S]{5,150}$";

    String subjectRegexp() default "^[\\s\\S]{3,150}$";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}