package offer.utils;

import offer.model.Offer;
import offer.model.Price;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestUtils {

    public static Offer createOffer(String id) {

        Offer offer = new Offer();
        offer.setId(id);
        offer.setCancelled(false);
        offer.setDescription("Offer 1 description");
        offer.setStartDate(DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now().plusMonths(3)));
        offer.setValidityTime(1000000);
        Price price = new Price();
        price.setAmount(new BigDecimal(100));
        price.setCurrency("GBP");
        offer.setPrice(price);
        return offer;
    }

}
