package offer.model;

import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PriceTest {
    private ValidatorFactory factory;
    private Validator validator;


    @Before
    public void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void priceShouldBeValid() {
        Price price = new Price();
        price.setAmount(new BigDecimal(10));
        price.setCurrency("GBP");

        List<String> validationMessage = validator.validate(price).stream()
                .map(constraint -> constraint.getPropertyPath() + " " + constraint.getMessage())
                .collect(Collectors.toList());

        assertTrue(validationMessage.isEmpty());
    }

    @Test
    public void priceShouldBeInvalidWhenMissingMandatoryFields() {
        List<String> validationMessage = validator.validate(new Price()).stream()
                .map(constraint -> constraint.getPropertyPath() + " " + constraint.getMessage())
                .collect(Collectors.toList());

        assertThat(validationMessage.size(), is(2));
        assertThat(validationMessage, hasItem("currency must not be null"));
        assertThat(validationMessage, hasItem("amount must not be null"));
    }
}
