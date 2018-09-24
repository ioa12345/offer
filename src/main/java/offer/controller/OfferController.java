package offer.controller;


import offer.exception.OfferExpiredException;
import offer.exception.OfferNotFound;
import offer.model.Offer;
import offer.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class OfferController {

    @Autowired
    private OfferRepository offerRepository;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> createOffer(@RequestBody @Valid Offer offer) {
        offerRepository.addOffer(offer);
        return ResponseEntity.ok("Offer created");
    }


    @GetMapping(value = "/retrieve/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Offer retrieveOffer(@PathVariable(value = "id") String id) throws OfferNotFound, OfferExpiredException {
        return offerRepository.getOffer(id);
    }

    @PutMapping(value = "/cancel/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> cancelOffer(@PathVariable(value = "id") String id) throws OfferNotFound {
        boolean isCancelled = offerRepository.cancellOffer(id);
        if (isCancelled) {
            return ResponseEntity.ok("Succesfully Cancelled");
        } else return ResponseEntity.badRequest().body("Unable To Cancell");
    }
}
