package offer.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class StartDateValidatorImpl implements ConstraintValidator<StartDateValidator, String> {

    @Override
    public void initialize(StartDateValidator constraintAnnotation) {
    }

    @Override
    public boolean isValid(String dateText, ConstraintValidatorContext constraintValidatorContext) {

        if (dateText == null) {
            return true;
        }

        try {
            LocalDate localDate = LocalDate.parse(dateText, DateTimeFormatter.ISO_DATE);
            if (localDate.isBefore(LocalDate.now())) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "cannot be in the past"
                ).addConstraintViolation();
                return false;
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
