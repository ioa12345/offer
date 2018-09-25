package offer.validator;


import offer.model.Offer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class OfferExpirationValidator {

    public boolean isExpired(Offer offer) {
        return LocalDate.now().isAfter(LocalDate.parse(offer.getStartDate(), DateTimeFormatter.ISO_DATE).plusDays(offer.getValidityTime())) ? true : false;
    }
}
