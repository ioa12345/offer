package offer.validator;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StartDateValidatorImplTest {

    private StartDateValidatorImpl startDateValidator = new StartDateValidatorImpl();

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Before
    public void setUp() {

    }

    @Test
    public void validWhen_offerIsNull() {
        assertTrue(startDateValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    public void validWhen_dateIsInFutureAndIsoFormat() {
        LocalDateTime localDateTime = LocalDateTime.now().plusMonths(3);
        String date = DateTimeFormatter.ISO_DATE_TIME.format(localDateTime);
        assertTrue(startDateValidator.isValid(date, constraintValidatorContext));

    }

    @Test
    public void invalidWhen_dateIsInFutureAndNotIsoFormat() {
        LocalDateTime localDateTime = LocalDateTime.now().plusMonths(3);
        String date = DateTimeFormatter.ofPattern("mm-dd-yy").format(localDateTime);
        assertFalse(startDateValidator.isValid(date, constraintValidatorContext));
    }

    @Test
    public void invalidWhen_dateIsInPastAndIsoFormat() {
        LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(3);
        String date = DateTimeFormatter.ISO_DATE_TIME.format(localDateTime);

        assertFalse(startDateValidator.isValid(date, constraintValidatorContext));
        verify(constraintValidatorContext).buildConstraintViolationWithTemplate(
                "cannot be in the past"
        );
    }

    @Test
    public void invalidWhenParsingAnInvalidString() {
        assertFalse(startDateValidator.isValid("invalid", constraintValidatorContext));
    }
}