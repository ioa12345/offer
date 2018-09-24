package offer.validator;


import offer.model.Offer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OfferExpirationValidator {

    public boolean isExpired(Offer offer) {
        return LocalDateTime.now().isAfter(LocalDateTime.parse(offer.getStartDate(), DateTimeFormatter.ISO_DATE_TIME).plusDays(offer.getValidityTime())) ? true : false;
    }
}
