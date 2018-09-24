package offer.model;

import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class OfferTest {
    private ValidatorFactory factory;
    private Validator validator;


    @Before
    public void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void offerShouldBeValid() {
        Offer offer = new Offer();
        offer.setId("offer1");
        offer.setCancelled(false);
        offer.setDescription("Offer 1 description");
        offer.setStartDate(DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now().plusMonths(3)));
        offer.setValidityTime(1000000);
        Price price = new Price();
        price.setAmount(new BigDecimal(100));
        price.setCurrency("GBP");
        offer.setPrice(price);
        List<String> validationMessage = validator.validate(offer).stream()
                .map(constraint -> constraint.getPropertyPath() + " " + constraint.getMessage())
                .collect(Collectors.toList());

        assertTrue(validationMessage.isEmpty());

    }

    @Test
    public void offerShouldBeInvalidWhenMissingMandatoryFields() {
        Offer offer = new Offer();

        List<String> validationMessage = validator.validate(offer).stream()
                .map(constraint -> constraint.getPropertyPath() + " " + constraint.getMessage())
                .collect(Collectors.toList());

        assertThat(validationMessage.size(), is(3));
        assertThat(validationMessage, hasItem("price must not be null"));
        assertThat(validationMessage, hasItem("validityTime must not be null"));
        assertThat(validationMessage, hasItem("description must not be null"));

    }

    @Test
    public void offerShouldBeInvalidWhenDateIsInThePast() {
        Offer offer = new Offer();
        offer.setId("offer1");
        offer.setCancelled(false);
        offer.setDescription("Offer 1 description");
        offer.setStartDate(DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now().minusMonths(3)));
        offer.setValidityTime(1000000);
        Price price = new Price();
        price.setAmount(new BigDecimal(100));
        price.setCurrency("GBP");
        offer.setPrice(price);
        List<String> validationMessage = validator.validate(offer).stream()
                .map(constraint -> constraint.getPropertyPath() + " " + constraint.getMessage())
                .collect(Collectors.toList());


        assertThat(validationMessage.size(), is(1));
        assertThat(validationMessage, hasItem("startDate cannot be in the past"));

    }

    @Test
    public void offerShouldBeInvalidWhenDateIsInvalid() {
        Offer offer = new Offer();
        offer.setId("offer1");
        offer.setCancelled(false);
        offer.setDescription("Offer 1 description");
        offer.setStartDate("invalid");
        offer.setValidityTime(1000000);
        Price price = new Price();
        price.setAmount(new BigDecimal(100));
        price.setCurrency("GBP");
        offer.setPrice(price);
        List<String> validationMessage = validator.validate(offer).stream()
                .map(constraint -> constraint.getPropertyPath() + " " + constraint.getMessage())
                .collect(Collectors.toList());


        assertThat(validationMessage.size(), is(1));
        assertThat(validationMessage, hasItem("startDate should be in ISO format"));

    }
}