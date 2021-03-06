package offer.repository;

import offer.exception.OfferExpiredException;
import offer.exception.OfferNotFoundException;
import offer.model.Offer;
import offer.utils.TestUtils;
import offer.validator.OfferExpirationValidator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class OfferRepositoryTest {


    private static final String OFFER1_KEY = "offer1";
    private static final String OFFER2_KEY = "offer2";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Mock
    private OfferExpirationValidator offerExpirationValidator;

    @InjectMocks
    OfferRepository offerRepository;

    private Offer offer;

    @Before
    public void setUp() {
        offer = TestUtils.createOffer(OFFER1_KEY);
        offerRepository.getAllOffers().put(OFFER1_KEY, offer);
    }

    @Test
    public void saveOffer_success() {
        offerRepository.getAllOffers().clear();

        offerRepository.addOffer(offer);

        assertThat(offerRepository.getAllOffers().get(OFFER1_KEY), is(offer));
    }

    @Test
    public void saveOffer_successWithNoStartDate() {
        offerRepository.getAllOffers().clear();

        offer.setStartDate(null);

        offerRepository.addOffer(offer);

        assertThat(offerRepository.getAllOffers().get(OFFER1_KEY), is(offer));
        assertThat(offerRepository.getAllOffers().get(OFFER1_KEY).getStartDate(), notNullValue());
    }

    @Test
    public void getOffer_success() throws OfferNotFoundException, OfferExpiredException {
        Offer actualOffer = offerRepository.getOffer(OFFER1_KEY);
        assertThat(actualOffer, is(offer));
    }

    @Test
    public void getOffer_shouldFailWhenOfferIsExpired() throws OfferNotFoundException, OfferExpiredException {
        Offer offer = TestUtils.createOffer(OFFER1_KEY);
        offer.setCancelled(true);
        offerRepository.addOffer(offer);

        expectedEx.expect(OfferExpiredException.class);
        expectedEx.expectMessage("Offer is already cancelled");

        offerRepository.getOffer(OFFER1_KEY);
    }


    @Test
    public void getOffer_shouldFailWhenOfferJustExpired() throws OfferNotFoundException, OfferExpiredException {
        when(offerExpirationValidator.isExpired(offer)).thenReturn(true);

        expectedEx.expect(OfferExpiredException.class);
        expectedEx.expectMessage("Offer has expired");

        offerRepository.getOffer(OFFER1_KEY);

        assertTrue(OfferRepository.getAllOffers().get(OFFER1_KEY).getCancelled());
    }


    @Test
    public void getOffer_shouldFailWhenOfferNotFound() throws OfferNotFoundException, OfferExpiredException {

        expectedEx.expect(OfferNotFoundException.class);
        expectedEx.expectMessage("Offer does not exist");

        offerRepository.getOffer(OFFER2_KEY);
    }

    @Test
    public void cancelOffer_success() throws OfferNotFoundException {
        assertTrue(offerRepository.cancellOffer(OFFER1_KEY));
        assertTrue(OfferRepository.getAllOffers().get(OFFER1_KEY).getCancelled());
    }

    @Test
    public void cancelOffer_shouldFailWhenOfferNotFound() throws OfferNotFoundException {
        expectedEx.expect(OfferNotFoundException.class);
        expectedEx.expectMessage("Cannot cancel an offer which does not exist");

        offerRepository.cancellOffer(OFFER2_KEY);

    }

}