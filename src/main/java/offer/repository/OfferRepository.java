package offer.repository;


import offer.exception.OfferExpiredException;
import offer.exception.OfferNotFoundException;
import offer.model.Offer;
import offer.validator.OfferExpirationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class OfferRepository {

    private static Map<String, Offer> offerMap;

    private OfferExpirationValidator offerExpirationValidator;

    @Autowired
    public OfferRepository(OfferExpirationValidator offerExpirationValidator) {
        this.offerExpirationValidator = offerExpirationValidator;
        offerMap = new HashMap<>();
    }

    public void addOffer(Offer offer) {
        String startDate = Optional.ofNullable(offer.getStartDate()).orElse(DateTimeFormatter.ISO_DATE.format(LocalDate.now()));
        offer.setStartDate(startDate);
        offerMap.put(offer.getId(), offer);
    }

    public Offer getOffer(String id) throws OfferExpiredException, OfferNotFoundException {
        if (offerMap.containsKey(id)) {

            Offer offer = offerMap.get(id);
            if (Optional.ofNullable(offer.getCancelled()).orElse(false)) {
                throw new OfferExpiredException("Offer is already cancelled");
            } else if (offerExpirationValidator.isExpired(offer)) {
                cancelExistingOffer(offer.getId());
                throw new OfferExpiredException("Offer has expired");
            }
            return offer;
        } else {
            throw new OfferNotFoundException("Offer does not exist");
        }

    }

    public boolean cancellOffer(String id) throws OfferNotFoundException {
        if (offerMap.containsKey(id)) {
            cancelExistingOffer(id);
            return true;
        }
        throw new OfferNotFoundException("Cannot cancel an offer which does not exist");
    }

    private void cancelExistingOffer(String id) {
        Offer offer = offerMap.get(id);
        offer.setCancelled(true);
        offerMap.put(id, offer);
    }

    protected static Map<String, Offer> getAllOffers() {
        return offerMap;
    }
}
