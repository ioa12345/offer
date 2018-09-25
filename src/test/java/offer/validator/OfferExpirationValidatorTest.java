package offer.validator;

import offer.model.Offer;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OfferExpirationValidatorTest {

    OfferExpirationValidator offerExpirationValidator = new OfferExpirationValidator();

    @Test
    public void isExpired_whenStartDatePlusValidityTimeIsBeforeToday() {
        Offer offer = new Offer();
        offer.setStartDate(DateTimeFormatter.ISO_DATE.format(LocalDate.now().minusMonths(3)));
        offer.setValidityTime(2);

        assertTrue(offerExpirationValidator.isExpired(offer));
    }

    @Test
    public void isNotExpired_whenStartDatePlusValidityTimeIsAfterToday() {
        Offer offer = new Offer();
        offer.setStartDate(DateTimeFormatter.ISO_DATE.format(LocalDate.now().minusMonths(3)));
        offer.setValidityTime(200);

        assertFalse(offerExpirationValidator.isExpired(offer));
    }
}