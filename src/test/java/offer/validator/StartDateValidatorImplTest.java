package offer.validator;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
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
        LocalDate localDate = LocalDate.now().plusMonths(3);
        String date = DateTimeFormatter.ISO_DATE.format(localDate);
        assertTrue(startDateValidator.isValid(date, constraintValidatorContext));

    }

    @Test
    public void invalidWhen_dateIsInFutureAndNotIsoFormat() {
        LocalDate localDate = LocalDate.now().plusDays(3);
        String date = DateTimeFormatter.ofPattern("MMM d yyyy").format(localDate);
        assertFalse(startDateValidator.isValid(date, constraintValidatorContext));
    }

    @Test
    public void invalidWhen_dateIsInPastAndIsoFormat() {
        LocalDate localDate = LocalDate.now().minusDays(3);
        String date = DateTimeFormatter.ISO_DATE.format(localDate);

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